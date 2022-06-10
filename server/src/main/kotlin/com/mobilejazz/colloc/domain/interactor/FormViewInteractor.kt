package com.mobilejazz.colloc.domain.interactor

class FormViewInteractor {
    operator fun invoke(): String {
        var html = """
            <!doctype html>
            <html lang="en">
            <head>
            <meta charset="UTF-8">
            <title>Colloc | Online tool to generate Localization Android, iOS and JSON files</title>
            </head>
            <body>
            <div>
            <h1>Colloc</h1>
            <form action="/colloc" target="_blank" method="get">
            <label for="link">Google Drive ID document</label><br>
            <input type="text" name="id"><br>
            <input type="radio" id="radio_android" name="platform" value="ANDROID">
            <label for="radio_android">ANDROID</label><br>
            <input type="radio" id="radio_ios" name="platform" value="IOS">
            <label for="radio_ios">IOS</label><br>
            <input type="radio" id="radio_json" name="platform" value="JSON">
            <label for="radio_json">JSON</label><br>
            <input type="radio" id="radio_angular" name="platform" value="ANGULAR">
            <label for="radio_angular">ANGULAR</label><br><input type="submit">
            </form>
            </div>
            </body>
            </html>
        """.trimIndent()

        return html
    }
}
