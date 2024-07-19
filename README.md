Playground: Clean Architecture Android App
==========
[![build](https://github.com/annasta13/playground/actions/workflows/build.yml/badge.svg)](https://github.com/annasta13/playground/actions/workflows/gradle.yml)
[![unit-test](https://github.com/annasta13/playground/actions/workflows/unit-test.yml/badge.svg)](https://github.com/annasta13/playground/actions/workflows/unit-test.yml)
[![GitHub license](https://img.shields.io/github/license/annasta13/playground.svg?style=plastic)](https://github.com/annasta13/playground/blob/master/LICENSE)
<a href="https://developer.android.com/about/versions/nougat/android-7.0"><img alt="API" src="https://img.shields.io/badge/minSdkVersion-24-yellow.svg?style=true"/></a>
<a href="https://developer.android.com/about/versions/14"><img alt="API" src="https://img.shields.io/badge/targetSdkVersion-34-green.svg?style=true"/></a>

## Use Case:
- Word search
  - List Query
- Data Processing
  - Product List
  - Product Detail
  - Transaction List
  - Create Transaction
- Animation
  - Heart Beat Animation
- API Fetching
  - Get Users
  - Create Users

## Preview
| Word search | Data Processing | Animation | API Fetching |
| --- | --- | --- | --- |
| <img src="https://raw.githubusercontent.com/annasta13/playground/master/screenshots/search-preview.gif" width=200/> | <img src="https://raw.githubusercontent.com/annasta13/playground/master/screenshots/data-processing-preview.gif" width=200/> | <img src="https://raw.githubusercontent.com/annasta13/playground/master/screenshots/heart-beat-animation-preview.gif" width=200/> | <img src="https://raw.githubusercontent.com/annasta13/playground/master/screenshots/api-fetching-preview.gif" width=200/> | 

## Libraries
- Jetpack Compose
- Retrofit
- Moshi
- Pagination
- Room Database

## Cloning and Running
### Prerequisites
1. [Android Studio][2] - min: Iguana
2. [Android 7 Device][3] - API Level 24 / Nougat

### Gradle Configuration
Gradle synchronization needs adjustment from local.properties. To synchronize gradle successfully, login to [Go Rest][1] and generate the access token. 
Then, add the access token to `local.properties` file as shown below.
```
ACCESS_TOKEN="c8abb81c572e6c3xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"
```

## Credits
- [Go Rest][1] - API Provider.

[1]: https://gorest.co.in/
[2]: https://developer.android.com/studio/
[3]: https://developer.android.com/tools/releases/platforms#7.0
