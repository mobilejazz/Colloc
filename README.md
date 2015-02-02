# Colloc
Colloc is a collaborative platform to manage the localizations for iOS and Android using a Google Document.

Basically, you can define all the localization strings of your project in a user-friendly google spreadsheet document as it is shown in the image.

![](images/sample_gspreadsheet.png)

Then, by runing a script you can export all the strings for all languages to iOS and/or Android format.

## Instalation

There is only one file needed: the `colloc.php` file. Therefore the options are:

- Download the `colloc.php` file manually and add it to your project.
- Use the Cocoa Pod to get the file:
```
pod 'Colloc', :git => 'https://github.com/mobilejazz/Colloc.git'
```

## 1. Defining the Google Spreadsheet

Create a new Google spreadsheet document and configure it as follows:

1. In the first row, add the word "key" at the column A and the symbol "#" at the column B.
2. Then, for each language you want to suport ad the name of it on each consequtive column (always at row 1). For example, add "English" at column C and "Spanish" at column D.

Now you can start adding the keys on the column "key" (column A) and all transaltions on the correpsonding cells. Do not add any text on the column "#" (column B).


## 2. Retrieving the Google Spreadsheet URL

The `colloc.php` script needs the Google spreadsheet exported as a `.tsv` file. You can use directly a local `.tsv` file or retrieve the Google spreadsheet exporting URL path. 

To get the exporting to `.tsv` URL just open the spreadsheet and select "File" > "Download as" > "Tab-spearated values (.tsv)". Your browser will download then the document. Then open the downloads manager and copy the download link from it.

## 3. Exporting to iOS

To export to iOS just create a new bash script and execute the `colloc.php` file with the appropiate arguments.

For example:

```
#!/bin/sh

##
# The Google Document .tsv file path.
# In this example we are using a local file. However you can point directly to the .tsv export of a google doc file.
# To get the url just export your spreadsheet in tsv format and retrieve the download url from your browser.
GDOC_PATH="<THE_URL_TO_THE_GOOGLE_SPREADSHEET_EXPORTED_AS_TSV>"

##
# The colloc script path from the pod path.
##
COLLOC_PATH="../../Pods/Colloc/colloc.php"


OUTPUT_FOLDER_NAME="." # Put the localization files in the same folder as the script
OUTPUT_TYPE="100" #100 for iOS export

exec "php" "$COLLOC_PATH" "$GDOC_PATH" "$OUTPUT_FOLDER_NAME" "$OUTPUT_TYPE"
```

When executing this script it will generate all the `en.lproj`, `es.lproj`, etc. containing the `Localizable.strings`files for each defiend language. Add them to your Xcode project and you are ready to go.

It is a good practice to create a *Languages* folder in your Xcode project and add inside the the custom script as well as all the `Localizable.string` generated files.

Also, **Colloc** will generate an auto-generated `Localization.h` file that includes defininitions of all translation entries:

```
#define <my_key> NSLocalizedString(@"my_key",nil)
```

Import this file in your `.pch` project file. This way, you can easily use your localized strings and Xcode will even provide autocompletion when using them.

## 4. Exporting to Android

To export to iOS just create a new bash script and execute the `colloc.php` file with the appropiate arguments.

For example:

```
#!/bin/sh

##
# The Google Document .tsv file path.
# In this example we are using a local file. However you can point directly to the .tsv export of a google doc file.
# To get the url just export your spreadsheet in tsv format and retrieve the download url from your browser.
GDOC_PATH="<THE_URL_TO_THE_GOOGLE_SPREADSHEET_EXPORTED_AS_TSV>"

##
# The colloc script path from the pod path.
##
COLLOC_PATH="../../Pods/Colloc/colloc.php"


OUTPUT_FOLDER_NAME="." # Put the localization files in the same folder as the script
OUTPUT_TYPE="010" #010 for Android export

exec "php" "$COLLOC_PATH" "$GDOC_PATH" "$OUTPUT_FOLDER_NAME" "$OUTPUT_TYPE"
```
When executing this script it will generate the Android localization files for all languages. Just import them to your project and you are ready to go.

---
# License

Copyright 2015 Mobile Jazz SL

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
