# Qr Core Helper

[![](https://jitpack.io/v/mobileSPE/Qr-Helper.svg)](https://jitpack.io/#mobileSPE/Qr-Helper)

An Indonesian Standard QR library for decode and processing raw qr string into readable & usable data in android

## Features
* Decode QR CPM
* Decode QRIS
* Auto decode QR CPM / QRIS
* Generate QR Bitmap from string
* Save Bitmap / View to device storage
* QR Scanner with auto decode (experimental)


## Preparation

**Step 1.** Add jitpack in your root build.gradle:
```kotlin
allprojects {
	repositories {
		..
		maven { url 'https://jitpack.io' }
	}
}
```

**Step 2.** Add the dependency
```kotlin
dependencies {
	implementation 'com.github.mobileSPE:Qr-Helper:latest-release'
}
```
  
## How to use
* Decode QR CPM
```kotlin
  QRCPMHelper().decode("your string") { model, rawJson ->
    val dataInModel = model.data
    val dataInString = raw.toString()
  }
```

* Decode QRIS
```kotlin
  QRISHelper().decode("your string") { model, rawJson ->
    val dataInModel = model.data
    val dataInString = raw.toString()
  }
```

* Auto decode QR string
```kotlin
  ScannerHelper().autoDecode("your string") { rawJson ->
    val dataInString = raw.toString()
  }
  
```

* Generate QR image from string
```kotlin
  val imageBitmap = GenerateQrHelper().generate("your string")
```

* Save Bitmap to device storage
```kotlin
  //save image with default prefix filename "img_spe_ ......" and directory name "Picture/"
  GenerateQrHelper().save(context, Data.BITMAP(imageInBitmap)) { status ->
      //display some toast based on status
  }
  
  //save image with custom prefix file name
  GenerateQrHelper().save(context, Data.BITMAP(imageInBitmap), "your_prefix_") { status ->
      //display some toast based on status
  }
  
   //save image with custom prefix file name and directory name
  GenerateQrHelper().save(context, Data.VIEW(imageInBitmap), "your_prefix_", "your directory") { status ->
      //display some toast based on status
  }
```

* QR Scanner
```kotlin
  //initialize scanner before use
  ScannerHelper().init(this)
  
  //display scanner
  ScannerHelper().display { result ->
      //do something with scan result
  }
```
