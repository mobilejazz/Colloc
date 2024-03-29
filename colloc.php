#!/usr/bin/php
<?php
/*
 * Copyright 2015 Mobile Jazz SL
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

function stringStartsWith($haystack, $needle)
{
    // search backwards starting from haystack length characters from the end
    return $needle === "" || strrpos($haystack, $needle, -strlen($haystack)) !== false;
}

function file_get_contents_utf8($fn)
{
    $arrContextOptions=array("ssl"=>array("verify_peer"=>false,
                                          "verify_peer_name"=>false,
                                          ),
                             );
    $content = file_get_contents($fn, false, stream_context_create($arrContextOptions));
    return mb_convert_encoding($content, 'UTF-8', mb_detect_encoding($content, 'UTF-8, ISO-8859-1', true));
}

$url = $argv[ 1 ];
echo("\nURL: " . $argv[ 1 ]);

$editUrl                  = str_replace('/pub?', '/ccc?', $url);
$editUrl                  = str_replace('&output=txt', '', $editUrl);
$editUrl                  = str_replace('&gid=0', '', $editUrl);
$editUrl                  = str_replace('&output=txt', '', $editUrl);
$GLOBALS[ "url_to_edit" ] = $editUrl;

$destPath = ".";

$width = shell_exec('tput cols');
if (!$width)
{
    $width = 80;
}
$outputDivider = str_repeat('-', (int) $width);

echo $outputDivider . "\n";

if ($argv[ 2 ])
{
    $destPath = $argv[ 2 ];
    echo("\nDestination Path: " . $destPath . "\n");
    echo $outputDivider . "\n";
} else {
    echo("\nWarning: EMPTY Destination Path, generating files in current directory\n");
}

if (!$argv[ 1 ])
{
    die("\n" . 'ERROR: Syntax: localizationTextsParse <url> [<destination_path>] [<formats>]' . "\n\n");
}

/*
 * argv 3 specifies which files get exported in a bitwise operator value:
 *
 * 100 exports only iOS
 * 010 exports only Android
 * 001 exports only JSON
 * 110 Exports iOS+Android
 *
 */

if (!$argv[ 3 ])
{
    $argv[ 3 ] = '111';
}

$localizationFileLines = file_get_contents_utf8($url);
//var_dump($localizationFileLines);

$localizationFileLines = explode("\n", $localizationFileLines);

$iOSFiles     = [ ];
$androidFiles = [ ];

if (count($localizationFileLines) > 0)
{
    $i     = 0;
    $lines = 0;

    $iOSFiles[ "swift" ][] = "import Foundation"."\n\n".
        "public protocol LocalizedEnum : CustomStringConvertible {}\n\n".
        "extension LocalizedEnum where Self: RawRepresentable, Self.RawValue == String {\n".
            "\tpublic var description : String  {\n".
                "\t\treturn NSLocalizedString(self.rawValue, comment: \"\")\n\t}\n}\n\n".
        "public enum Colloc: String, LocalizedEnum {";

    foreach ($localizationFileLines as $line)
    {
        if (trim($line) == "") // Get rid of empty lines
        {
            continue;
        }

        $line = str_replace("\r", '', $line);

        $fields = explode("\t", $line);

        $key    = $fields[ 0 ];
        $values = array_slice($fields, 1);

        if ($i == 0)
        {
            $languages = $values;
            //var_dump($languages);
        }
        else
        {
            $lineIsAComment = count($values) == 0 || stringStartsWith($key, '#');
            // if key contains spaces is considered a comment
            $keyContainsWhitespaces = strpos($key, ' ');
            $merged_values          = implode("", $values);

            if (!$lineIsAComment && !$keyContainsWhitespaces && strlen($key) > 0 && strlen($merged_values) > 0) // It's not a comment and it's not empty
            {

                //echo $key,',';
                $lines++;

                $languageIndex = 0;



                foreach ($values as $value)
                {
                    $iOSParsedLine     = iOSLineParse($key, $value);
                    $androidParsedLine = androidLineParse($key, $value);

                    $languageName = $languages[ $languageIndex ];

                    if ($languageName != '#')
                    {
                        $iOSFiles[ $languageName ][]                                 = $iOSParsedLine;
                        $androidFiles[ $languageName ][]                             = $androidParsedLine;
                        $jsonFiles[ convertLanguageToISO639($languageName) ][ $key ] = $value;
                    }

                    $languageIndex++;
                }

                $iOSFiles[ "header" ][] = "#define $key NSLocalizedString(@\"$key\", nil)";
                $iOSFiles[ "swift" ][] = "\tcase $key";


            }
            else
            {
                $iOSParsedComment     = iOSCommentParse($key);
                $androidParsedComment = androidCommentParse($key);

                foreach ($languages as $lang)
                {
                    if ($lang != '#')
                    {
                        $iOSFiles[ $lang ][]     = $iOSParsedComment;
                        $androidFiles[ $lang ][] = $androidParsedComment;
                    }
                }
            }
        }

        $i++;
    }

    $iOSFiles[ "swift" ][] = "}\n\ntypealias 🌏 = Colloc";


    echo "\n" . $outputDivider . "\n";
    echo "Lines: " . count($localizationFileLines);
    echo "\n" . $outputDivider . "\n\n";

    if ($argv[ 3 ][ 0 ] == '1')
    {
        echo("\nWriting IOS Files");
        writeIOSFiles($iOSFiles, $destPath);
    }
    if ($argv[ 3 ][ 1 ] == '1')
    {
        echo("\nWriting Android Files");
        writeAndroidFiles($androidFiles, $destPath);
    }
    if ($argv[ 3 ][ 2 ] == '1')
    {
        echo("\nWriting JSON files");
        writeJSONFiles($jsonFiles, $destPath);
    }

}
else
{
    die("Error opening file $localizationFileName");
}

function iOSLineParse($key, $localizedString)
{
    $localizedString = str_replace('"', '\"', $localizedString);
    $localizedString = preg_replace('/%[0-9]+\$/', '%', $localizedString);
    $localizedString = str_replace('%s', '%@', $localizedString);

    return "\"$key\" = \"$localizedString\";";
}

$occurencesCounter = 0;
function androidLineParse($key, $localizedString)
{
    // replace iOS string occurence to android format
    $localizedString = str_replace("%@", "%s", $localizedString);
    // if the string starts with @, escapes it: \@
    $localizedString = preg_replace("/^(@)/", "\\@", $localizedString);

    // replace multiple arguments to android format. Example:
    //  input: %s te ha enviado %s minuto de felicidad
    //  output: %1$s te ha enviado %2$s minuto de felicidad
    resetOccurencesCounter();
    $localizedString = preg_replace_callback("/%([\.a-z])/", "replaceArgumentsIntoAndroidFormat", $localizedString);

    $localizedString = str_replace("'", "\'", $localizedString);
    $localizedString = str_replace("&", "&amp;", $localizedString);
    $localizedString = str_replace("<", "&lt;", $localizedString);
    $localizedString = str_replace(">", "&gt;", $localizedString);
    $localizedString = str_replace('"', '\"', $localizedString);

    // Add more rules here.

    return "\t<string name=\"" . $key . "\">" . $localizedString . "</string>";
}

function resetOccurencesCounter()
{
    global $occurencesCounter;
    $occurencesCounter = 0;
}

function replaceArgumentsIntoAndroidFormat($occurrences)
{
    global $occurencesCounter;

    return "%" . ++$occurencesCounter . "$" . $occurrences[ 1 ];
}

function iOSCommentParse($comment)
{
    return "\n// " . $comment;
}

function androidCommentParse($comment)
{
    return "\n\t<!--" . $comment . "-->";
}

function writeIOSFiles($files, $destPath)
{
    $iOSPath = $destPath;

    $CatPath        = "ca.lproj";
    $EnglishPath    = "en.lproj";
    $SpanishPath    = "es.lproj";
    $GermanPath     = "de.lproj";
    $CzechPath      = "cs.lproj";
    $GreekPath      = "el.lproj";
    $FrenchPath     = "fr.lproj";
    $ItalianPath    = "it.lproj";
    $PortuguesePath = "pt.lproj";
    $DutchPath      = "nl.lproj";
    $SwedishPath    = "sv.lproj";
    $ArabicPath     = "ar.lproj";
    $PolandPath     = "pl.lproj";
    $PortuguesePath = "pt.lproj";
    $RussianPath    = "ru.lproj";
    $TurkishPath    = "tr.lproj";
    $ChinesePath    = "zh.lproj";
    $JapanesePath   = "ja.lproj";

    foreach ($files as $languageName => $lines)
    {
        $directory = "";

        echo("\nIn language: " . $languageName);

        if (strcmp($languageName, "Catalan") == 0)
        {
            $directory = $CatPath;
        }
        else if ($languageName == "English")
        {
            $directory = $EnglishPath;
        }
        else if ($languageName == "Spanish")
        {
            $directory = $SpanishPath;
        }
        else if ($languageName == "German")
        {
            $directory = $GermanPath;
        }
        else if ($languageName == "Czech")
        {
            $directory = $CzechPath;
        }
        else if ($languageName == "Greek")
        {
            $directory = $GreekPath;
        }
        else if ($languageName == "French")
        {
            $directory = $FrenchPath;
        }
        else if ($languageName == "Italian")
        {
            $directory = $ItalianPath;
        }
        else if ($languageName == "Portuguese")
        {
            $directory = $PortuguesePath;
        }
        else if ($languageName == "Dutch")
        {
            $directory = $DutchPath;
        }
        else if ($languageName == "Swedish")
        {
            $directory = $SwedishPath;
        }
        else if ($languageName == "Arabic")
        {
            $directory = $ArabicPath;
        }
        else if ($languageName == "Polish")
        {
            $directory = $PolandPath;
        }
        else if ($languageName == "Portuguese")
        {
            $directory = $PortuguesePath;
        }
        else if ($languageName == "Russian")
        {
            $directory = $RussianPath;
        }
        else if ($languageName == "Turkish")
        {
            $directory = $TurkishPath;
        }
        else if ($languageName == "Chinese")
        {
            $directory = $ChinesePath;
        }
        else if ($languageName == "Japanese")
        {
            $directory = $JapanesePath;
        }
        else
        {
            $directory = preg_replace('/\s+/', '', $languageName) . ".lproj";
        }

        $filename = $iOSPath . "/" . $directory . "/Localizable.strings";

        if ($languageName == "swift")
        {
            $filename = $iOSPath . "/" . "Colloc.swift";
        }

        if ($languageName == "header")
        {
            $filename = $iOSPath . "/" . "Localization.h";
        }

        echo("iOS  - Trying to Write:\n" . $filename . "\n");
        createPathIfDoesntExists($filename);
        $fh = fopen($filename, "w");

        if ($fh !== false)
        {
            fwrite($fh,
                "/**\r\tThis is an automatically generated file\r\tPlease don't modify it." . "\r\tIf you need to change some text please do so at\r\t" . $GLOBALS[ "url_to_edit" ] . "\r\t*/\r\r");

            foreach ($lines as $line)
            {
                fwrite($fh, $line . "\n");
            }

            fclose($fh);
            echo "iOS  - " . chr(27) . "[1;32m" . 'Done' . chr(27) . "[0m" . "\n\n";
        }
        else
        {
            echo "iOS  - Error opening file $filename to write\n\n";
        }
    }
}

function writeAndroidFiles($files, $destPath)
{
    $androidPath = "$destPath/values";

    foreach ($files as $languageName => $lines)
    {
        $languageCode = convertLanguageToISO639($languageName);
        if (!$languageCode)
        {
            continue;
        }
        $filenameLanguageCode = $languageCode == "en" ? "" : "-" . $languageCode;
        $filename             = $androidPath . $filenameLanguageCode . "/strings.xml";
        echo("ANDR - Trying to Write:\n" . $filename . "\n");
        createPathIfDoesntExists($filename);
        $fh = fopen($filename, "w");

        if ($fh !== false)
        {
            fwrite($fh, "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
            fwrite($fh, "<resources>\n\n");

            fwrite($fh,
                "<!--\r\n\tThis is an automatically generated file\r\n\tPlease don't modify it." . "\r\n\tIf you need to change some text please do so at\r\n\t" . $GLOBALS[ "url_to_edit" ] . "\r\n\t-->\r\n\r\n");

            foreach ($lines as $line)
            {
                fwrite($fh, $line . "\n\n");
            }

            fwrite($fh, "\n</resources>");

            fclose($fh);

            echo "ANDR - " . chr(27) . "[1;32m" . 'Done' . chr(27) . "[0m" . "\n\n";
        }
        else
        {
            echo "ANDR - Error opening file $filename to write\n\n";
        }
    }
}

function writeJSONFiles($files, $destPath)
{

    $filename = $destPath . '/stringsFromApp.json';
    echo("JSON - Trying to Write:\n" . $filename . "\n");
    createPathIfDoesntExists($filename);

    $fh = fopen($filename, "w");
    if ($fh !== false)
    {
        fwrite($fh, json_encode($files));
        echo "JSON - " . chr(27) . "[1;32m" . 'Done' . chr(27) . "[0m" . "\n\n";
    }
    else
    {
        echo "JSON - Error opening file $filename to write\n\n";
    }

}

function createPathIfDoesntExists($filename)
{
    $dirname = dirname($filename);
    if (!is_dir($dirname))
    {
        mkdir($dirname, 0755, true);
    }
}

function convertLanguageToISO639($language)
{

    $languages[ 'Catalan' ]    = "ca";
    $languages[ 'English' ]    = "en";
    $languages[ 'Spanish' ]    = "es";
    $languages[ 'German' ]     = "de";
    $languages[ 'Czech' ]      = "cs";
    $languages[ 'Greek' ]      = "el";
    $languages[ 'French' ]     = "fr";
    $languages[ 'Italian' ]    = "it";
    $languages[ 'Portuguese' ] = "pt";
    $languages[ 'Dutch' ]      = "nl";
    $languages[ 'Swedish' ]    = "sv";
    $languages[ 'Arabic' ]     = "ar";
    $languages[ 'Polish' ]     = "pl";
    $languages[ 'Portuguese' ] = "pt";
    $languages[ 'Russian' ]    = "ru";
    $languages[ 'Turkish' ]    = "tr";
    $languages[ 'Chinese' ]    = "zh";
    $languages[ 'Japanese' ]   = "ja";

    if (isset($languages[ $language ]))
    {
        echo "\nLANG: " . $language . "[" . $languages[ $language ] . "]";

        return $languages[ $language ];
    }

    echo "\nLanguage not found: " . $language;

    return "not_found";
}
