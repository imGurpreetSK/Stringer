<h1 align="center">Stringer</h1>
<h3 align="center">Create localised string files for Android and iOS from a single CSV file.</h3>


#### Working
Run `kscript stringer.kt <Name of your csv file>.csv`. Files will be generated in the same directory.

See [kscript](https://github.com/holgerbrandl/kscript) for installation and more.


*OR*

1. Create the csv named `strings.csv` and place it in `$homeDirectory/Downloads`. The resulting file path should be `$homeDirectory/Downloads/strings.csv`.
2. Execute [`main()`](https://github.com/GurpreetSK95/Stringer/blob/master/src/main/kotlin/com/gurpreetsk/Main.kt) function.
3. The generated files are located in `$homeDirectory/Desktop/StringerThings` directory.


#### Features
See [sample csv](https://github.com/GurpreetSK95/Stringer/blob/master/strings.csv) for format.

1. Templating is supported: Any string between `<` and `>` will be replaced with `%s` for Android and `%@` for iOS.
2. Any line starting with `#` is treated as comment.
3. A `type` and `feature` can be provided and resource key is generated accordingly for them. Eg: The generated key for `no connection, No internet connection found, ERROR, user details` will be `error_user_details_no_connection`.

<b>Note</b>: Ordering matters. CSVs must be ordered as `KEY, VALUE, TYPE (optional), FEATURE NAME (optional)`
