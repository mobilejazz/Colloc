#!/bin/sh

###############################################################################################
# THIS IS A SAMPLE SCRIPT TO EXEUCTE COLLOC WITHIN AN iOS PROJECT                             #
# EDIT THE MISING FIELDS BY PROVIDING A GDOC PATH                                             #
# THIS SCRIPT CAN BE COPIED AS A "RUN SCRIPT" OF A CUSTOM TARGET                              #
# FOR MORE INFORMATION, CHECKOUT THE README OF COLLOC AT https://github.com/mobilejazz/Colloc #
###############################################################################################

##
# The Google Document .tsv file path
##

GDOC_PATH="" # <-- TO BE FILLED

# In this example we are using a local file:
# GDOC_PATH="${SRCROOT}/TSV_FILE_NAME.tsv"

# However you can point directly to the .tsv export of a google doc file. 
# To get the url just export your spreadsheet in tsv format and retrieve the download url from your browser:
# GDOC_PATH="https://docs.google.com/a/ORGANIZATION/spreadsheets/d/DOCUMENT_ID/export?format=tsv"

##
# The colloc script path from the pod path.
##

COLLOC_PATH="${SRCROOT}/Pods/Colloc/colloc.php" # <-- Default cocoa pod pod structure

##
# Put the localization files in the SRCROOT/Languages folder
##

OUTPUT_FOLDER_NAME="${SRCROOT}/Languages" # <-- Edit this to save files into another folder

##
# Configure the output type as iOS export
##

OUTPUT_TYPE="100" # 100 means only iOS export

##
# Finally, executing the script
##

# 1. Printing the command being executed
echo "exec \"php\" \"$COLLOC_PATH\" \"$GDOC_PATH\" \"$OUTPUT_FOLDER_NAME\" \"$OUTPUT_TYPE\""

# 2. Executing the command
exec "php" "$COLLOC_PATH" "$GDOC_PATH" "$OUTPUT_FOLDER_NAME" "$OUTPUT_TYPE"