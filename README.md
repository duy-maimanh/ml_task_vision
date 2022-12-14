[![](https://jitpack.io/v/duy-maimanh/ml_task_vision.svg)](https://jitpack.io/#duy-maimanh/ml_task_vision)

# ML Task Vision!

This library help you solve common computer vision tasks by using machine learning. Such as blur nsfw images, censor faces, etc...

## How to use

Add it in your *settings.gradle*:
```
dependencyResolutionManagement {  
  repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)  
    repositories {  
	...  
    maven { url 'https://jitpack.io' }  
 }}
 ```

Add the dependency:

```
dependencies {
	implementation 'com.github.duy-maimanh:ml_task_vision:0.0.1'
}
 ```

##  Setup BaseOptions.

```
val options = BaseOptions.builder()  
    .isUseGPU(false)  
    .setNumberThreads(4)  
    .build()
 ```
 
The *setNumberThreads* is the settings for CPU that mean it only work when the *isUseGPU = false*. But you should set it because some devices not support GPU so they will use CPU instead.

# NSFW detection

This is very first library's feature. That help us to detect the image and know if it is nsfw or safe for work.
It is useful for kind of apps like social media, chatting, or dating...
The accuracy now around 88%. And I am continue training this model and will be update when I got higher accuracy.

## Download model
There are two ways to download models.

### Download manually.

Download [the model]() and [the label](). And copy these files to *asset* folder.

### Create *Gradle task* to auto download.
Open project level build.gradle. Add *de.undercouch:gradle-download-task:4.1.2*
```
buildscript {
    dependencies {
        ...
        classpath 'de.undercouch:gradle-download-task:4.1.2'
    }
}
```
Create *download_model.gradle* at *app* folder. And copy code bellow.

```
task downloadNSFWModel(type: Download) {
    src 'https://github.com/duy-maimanh/ml_task_vision/releases/download/0.0.1/nsfw_classifier.tflite'
    dest project.ext.ASSET_DIR + '/nsfw_classifier.tflite'
    overwrite false
}
task downloadNSFWLabel(type: Download) {
    src 'https://github.com/duy-maimanh/ml_task_vision/releases/download/0.0.1/nsfw_labels.txt'
    dest project.ext.ASSET_DIR + '/nsfw_labels.txt'
    overwrite false
}

preBuild.dependsOn downloadNSFWModel, downloadNSFWLabel
```

Move to app level build.gradle. Apply plugin and download task.
```
apply plugin: 'de.undercouch.download'

android {
	...
}

// import DownloadModels task
project.ext.ASSET_DIR = projectDir.toString() + '/src/main/assets'

apply from:'download_model.gradle'

dependencies {
	...
}
```
## NsfwProcessOptions

```
val nsfwProcessOptions = NsfwProcessOptions.builder()  
    .setBaseOptions(options) // if you don't use BaseOptions skip this line. 
    .setFilterMode(FilterMode.BLUR, 0.0) // The larger the number, the more blurred the image. 
    .build()
 ```

## Create Nsfw Processer
```
val nsfwProcess = NsfwProcess.create(context, nsfwProcessOptions)
```
## Detect
```
val result = nsfwProcess.detect(uri)
```
or
```
val result = nsfwProcess.detect(bitmap)
```

The result contains the confidence of each label (safe and unsafe) and the filtered image.

## NSFW Detection Benchmark

Run benchmark with image 1920*1080 for 50 times:

|                |Samsung S10 5G (Exynos 9820)                          | Realme Q5 Pro (snap 870)                         |
|----------------|-------------------------------|-----------------------------|
|Without blur effect| 50ms        |30ms            |
|With blur effect 0.5          |55ms            | 40ms          |
|With blur effect 1          |60ms|50ms|

# Face censor (Coming soon)
