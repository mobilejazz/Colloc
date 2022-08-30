# New New Colloc

## Building

    bin/build.sh

## Running

    docker-compose up -d

# Creating a localization spreadsheet

Create a new spreadsheet from the 
[Colloc localized strings template](https://docs.google.com/spreadsheets/d/17_X5tYnMWSrDxW_Zk67raPzQRMAORjwcDfze7tHyhm8/template/preview).

The newly created file will have an ID that you can get from the URL, for example `https://docs.google.
com/a/organization/spreadsheets/d/1MU_EXAMPLEEXAMPLEEXAMPLEEXAMPLE/edit`. 
Take a note, because you will use it in the next step.

Make the document publicly accessible, by clicking on **Share**, then in **General Access** change the
setting to **Anyone with the link** is a **Viewer**.

# Installing Colloc on an Angular project

You can get a set of translation files in the format that `ngx-translate` needs as a zip like this:

    curl --output translations.zip 'https://colloc.mobilejazz.dev/colloc?id=1MU_EXAMPLEEXAMPLEEXAMPLEEXAMPLE&platform=ANGULAR' && \
    unzip translations.zip

You can even add this to your `package.json` as an option for `npm run`.

# Installing Colloc on an iOS project

Add a target "Localizable Strings" with build phase like this:

    #!/bin/sh
    
    # The Google Document ID. You can take it from the URL.
    # This document must be publicly accessible.
    GDOC_ID="1ACOd7GoXovJ0vNQp2REmvKUJMwln2qFw-yElwz-gr5U"
    OUTPUT_FOLDER_NAME="${SRCROOT}/Anapphylaxis/Resources/Localizations"
    
    curl "https://colloc.mobilejazz.dev/colloc?id=${GDOC_ID}&platform=IOS" > colloc.zip
    unzip -o colloc.zip -x / -d "${OUTPUT_FOLDER_NAME}"
    rm colloc.zip
