#!/bin/sh

##
# The Google Document .tsv file path.
# In this example we are using a local file. However you can point directly to the .tsv export of a google doc file.
# To get the url just export your spreadsheet in tsv format and retrieve the download url from your browser.
GDOC_PATH="<THE_URL_TO_THE_GOOGLE_SPREADSHEET_EXPORTED_AS_TSV>"

##
# The colloc script path from the pod path.
##
COLLOC_PATH="./colloc.php"


OUTPUT_FOLDER_NAME="colloc_output" # Put the localization files in the same folder as the script
OUTPUT_TYPE="010" #010 for Android export,100 generates iOS, 110 generates iOS + Android output

exec "php" "$COLLOC_PATH" "$GDOC_PATH" "$OUTPUT_FOLDER_NAME" "$OUTPUT_TYPE"
