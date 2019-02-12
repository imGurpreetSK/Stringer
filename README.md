<h1 align="center">Stringer</h1>
<h3 align="center">Create localised string files for Android and iOS from a single CSV file.</h3>


#### Working
Run `kscript Program.kt <Name of your csv file>.csv`. Files will be generated in the same directory.

See [Kscript](https://github.com/holgerbrandl/kscript) for installation and more.


*OR*

1. Create the csv named `errors.csv` and place it in `$homeDirectory/Downloads`. The resulting file path should be `$homeDirectory/Downloads/errors.csv`.
2. Execute [`main()`](https://github.com/GurpreetSK95/Stringer/blob/master/src/main/kotlin/com/gurpreetsk/Main.kt) function.
3. The generated files are located in `$homeDirectory/Desktop/StringerThings` directory.


#### CSV format
See [sample csv](https://github.com/GurpreetSK95/Stringer/blob/master/SampleErrors.csv).

Templating is supported: Any string between `<` and `>` will be replaced with `%s` for Android and `%@` for iOS.


#### TODOs
1. :heavy_check_mark: ~Convert to a kotlin script which can be run from terminal.~ Try fixing compilation errors and running main() using kscript.
2. Configurable source file path and destination folder.
3. Write missing tests - no TDD followed.
4. Steps to run without kscript (?)
