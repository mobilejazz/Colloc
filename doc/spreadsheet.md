# Defining the Google Spreadsheet

Create a new Google spreadsheet document and configure it as follows:

1. The first column is used to store the keys of your strings. Keys must have only characters `A-Z`, `a-z`, `0-9` and `_`. Using any other character spaces or `-` may result in a building failure in your project. 
2. The first row of the first column must contain the title `Key` to indicate to the script that column contains keys.
3. For each language you want to support, fill a new column with its translations.
4. Use the first row of a language-column to indicate the name of language: `English`, `Spanish`, etc. Check on section 4 the supported languages.
5. Adding the char `#` in the first row of a column will indicate the script to ignore that column. Use this to add "comments" columns, "char counts" columns, etc.
6. Adding the char `#` as the first character of the first column of a row will indicate the script to ignore that row. Use this to ignore a specific key with its translations.
7. The `#` char can be used inside translations.
8. To add placeholders in your strings (for adding in runtime numbers, strings, ...) use the conventions listed in the table below. Colloc will take care to convert them to the platform-specific (iOS/Android) standard.

| Type            | Colloc | Example Key             | Example Translation              |
|-----------------|--------|-------------------------|----------------------------------|
| string          | %@     | ls_user_name            | My username is %@                |
| integer         | %d     | ls_user_age             | My age is %ld                    |
| float           | %f     | ls_city_distance_meters | The city is %fm away             |
| float precision | %.2f   | ls_number_two_decimals  | A number with two decimals: %.2f |


## Best Practices

- Create a "generic" set of translations to handle generic strings: "done", "save", "back", "cancel", "dismiss", etc.
- It is a good practice to use a reverse domain notation to define the names of your keys. This will be useful to group them by module or category and to find them when using Xcode autocompletion.
- Also, it is a very good pratcie to start all your keys with a prefix. For example "tr_" or "ls_" (from translation and localized string).

For example:
```
ls_generic_done
ls_generic_save
ls_generic_cancel
[...]
ls_profile_title
ls_profile_user_name
ls_profile_user_description
ls_profile_edit_user_name_placeholder
ls_profile_edit_user_description_placeholder
[...]
```

## TSV file

Colloc doesn't use the spreadsheet file directly. Instead, it uses a Tab Separated Values file. An example of such text file can be seen [in the iOS Sample](Sample%20Projects/iOS%20Sample/iOS%20Sample/Languages/sampleGDocFile.tsv)