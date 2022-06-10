package com.mobilejazz.colloc.domain.interactor

import org.springframework.stereotype.Service

@Service
class FormViewInteractor {
    operator fun invoke(): String {
        var html = """
            <!doctype html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Colloc | Online tool to generate Localization Android, iOS and JSON files</title>
                    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0-beta1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-0evHe/X+R7YkIZDRvuzKMRqM+OrBnVFBL6DOitfPri4tjfHxaWutUpFmBp4vmVor" crossorigin="anonymous">

            </head>
            <body>
            <div class="container">
  <div class="row">
    <div class="col">
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
  </div>
</div>

<div class="container">
  <div class="row">
    <div class="col">
    <p>Copyright 2022 © Mobile Jazz<br>
    <a target="_blank" href="https://github.com/mobilejazz/Colloc">GitHub</a>
    </p>
    </div>
      </div>
    </div>
                <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0-beta1/dist/js/bootstrap.bundle.min.js" integrity="sha384-pprn3073KE6tl6bjs2QrFaJGz5/SUsLqktiwsUTF55Jfv3qYSDhgCecCxMW52nD2" crossorigin="anonymous"></script>

            </body>
            </html>
        """.trimIndent()

        return html
    }
}
