#!/bin/sh

##
# The Google Document .tsv file path.
# In this example we are using a local file. However you can point directly to the .tsv export of a google doc file.
# To get the url just export your spreadsheet in tsv format and retrieve the download url from your browser.
GDOC_PATH="sampleGDocFile.tsv"

##
# The colloc script path from the pod path.
##
COLLOC_PATH="../../Pods/Colloc/colloc.php"


OUTPUT_FOLDER_NAME="." # Put the localization files in the same folder
OUTPUT_TYPE="100" #100 for iOS

exec "php" "$COLLOC_PATH" "$GDOC_PATH" "$OUTPUT_FOLDER_NAME" "$OUTPUT_TYPE"
