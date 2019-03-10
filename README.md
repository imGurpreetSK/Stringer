<h1 align="center">Stringer</h1>
<h3 align="center">Create localised string files for Android and iOS from a single CSV file.</h3>

[![Android Weekly]( https://img.shields.io/badge/Android%20Weekly-%23352-blue.svg )]( http://androidweekly.net/issues/issue-352 )

#### Why?
As an application grows, maintaining the same product communication gets harder on multiple platforms. Stringer solves this problem by generating platform specific string files from a common CSV file.
Currently, the generated files include `strings.xml` for Android and `localizable.strings` for iOS.

#### Working
Run `kscript stringer.kt <Path to your csv file>.csv`. Files will be generated in a directory named `StringerThings` on the same path.

See [kscript](https://github.com/holgerbrandl/kscript) for installation and more information.


*OR*

1. Create the csv named `mobile-strings.csv` and place it in `$homeDirectory/Downloads`. The resulting file path should be `$homeDirectory/Downloads/strings.csv` (default path of a downloaded file).
2. Execute [`main()`](https://github.com/GurpreetSK95/Stringer/blob/master/src/main/kotlin/com/gurpreetsk/Main.kt) function.
3. The generated files are located in `$homeDirectory/Desktop/StringerThings` directory.


#### Features
See [sample csv](https://github.com/GurpreetSK95/Stringer/blob/master/mobile-strings.csv) for format.

1. Templating is supported: Any string between `<` and `>` will be replaced with `%s` for Android and `%@` for iOS.
2. Any line starting with `#` is treated as comment.
3. A `type` and `feature` can be provided and resource key is generated accordingly for them. Eg: The generated key for `no connection, No internet connection found, ERROR, user details` will be `error_user_details_no_connection`.

<b>Note</b>: Ordering matters. CSVs must be ordered as `KEY, VALUE, TYPE (optional), FEATURE NAME (optional)`

License
---------------------

    Copyright 2019 Gurpreet Singh

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
