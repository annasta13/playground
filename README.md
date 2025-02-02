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
- Tensorflow Object Detector
  - Mobile Net V1
  - Efficient Det V0
  - Efficient Det V1
  - Efficient Det V2
  - Yolo V9

## Preview
| Word search | Data Processing | Animation | API Fetching |
| --- | --- | --- | --- |
| <img src="https://raw.githubusercontent.com/annasta13/playground/master/screenshots/search-preview.gif" width=200/> | <img src="https://raw.githubusercontent.com/annasta13/playground/master/screenshots/data-processing-preview.gif" width=200/> | <img src="https://raw.githubusercontent.com/annasta13/playground/master/screenshots/heart-beat-animation-preview.gif" width=200/> | <img src="https://raw.githubusercontent.com/annasta13/playground/master/screenshots/api-fetching-preview.gif" width=200/> | 

| Object Detector Options                                                                                             | Object Scanning Screen                                                                                                       |
|---------------------------------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------|
| <img src="https://raw.githubusercontent.com/annasta13/playground/master/screenshots/object-scanning-option-screen.webp" width=200/> | <img src="https://raw.githubusercontent.com/annasta13/playground/master/screenshots/object-scanning-screen.webp" width=200/> | 


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
3. API Fetching Demo: Gradle synchronization needs adjustment from local.properties. To synchronize gradle successfully, login to [Go Rest][1] and generate the access token.
   Then, add the access token to `local.properties`. Example: `ACCESS_TOKEN="c8abb81c572e6c3xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"`.
4. Tensorflow Demo: `buildSrc/src/main/kotlin/DownloadTasksPlugin.kt` task should be successfully executed. This execution runs before compiling.

### API Fetching
Gradle synchronization needs adjustment from local.properties. To synchronize gradle successfully, login to [Go Rest][1] and generate the access token. 
Then, add the access token to `local.properties` file as shown below.
```
ACCESS_TOKEN="c8abb81c572e6c3xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"
```

## Credits
- [Go Rest][1] - API Provider.
- [Tensorflow][9] - Tensorflow
- [Tensorflow Example][4] - Tensorflow Example
- [Yolo V9 Example][5] - Yolo V9 Example
- [Efficientdet][6] - Efficient Det
- [Yolo V9][7] - Yolo V9 
- [Yolo V5][7] - Yolo V5 

[1]: https://gorest.co.in/
[2]: https://developer.android.com/studio/
[3]: https://developer.android.com/tools/releases/platforms#7.0
[4]: https://github.com/tensorflow/examples/tree/master/lite/examples/object_detection
[5]: https://github.com/surendramaran/YOLO/blob/main/YOLOv10-Object-Detector-Android-Tflite/app/src/main/assets/yolov10n_float16.tflite
[6]: https://www.kaggle.com/models/tensorflow/efficientdet
[7]: https://docs.ultralytics.com/models/yolov9/
[8]: https://github.com/ultralytics/yolov5
[9]: https://www.tensorflow.org/
