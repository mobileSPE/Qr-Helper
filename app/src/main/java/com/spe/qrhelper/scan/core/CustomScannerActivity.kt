package com.spe.qrhelper.scan.core

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.KeyEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.MultiFormatReader
import com.google.zxing.RGBLuminanceSource
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.CaptureManager
import com.journeyapps.barcodescanner.MixedDecoder
import com.spe.qrhelper.common.Constant
import com.spe.qrhelper.R
import com.spe.qrhelper.databinding.ActivityCustomScannerBinding


open class CustomScannerActivity : AppCompatActivity() {

    private lateinit var bind: ActivityCustomScannerBinding
    private var capture: CaptureManager? = null
    private var flashOn: Bitmap? = null
    private var flashOff: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityCustomScannerBinding.inflate(layoutInflater)
        setContentView(bind.root)

        initView(savedInstanceState)
        initListener()
    }

    private fun initView(savedInstanceState: Bundle?) {
        bind.zxingBarcodeScanner.statusView.visibility = View.GONE

        if (!hasFlash()) {
            bind.switchFlashlight.visibility = View.GONE
        }

        capture = CaptureManager(this, bind.zxingBarcodeScanner)
        capture!!.initializeFromIntent(intent, savedInstanceState)
        capture!!.setShowMissingCameraPermissionDialog(true, getString(R.string.something_went_wrong))
        capture!!.decode()

        bind.tvTitle.text = intent?.getStringExtra(Constant.Intent_HeaderTitle) ?: getString(R.string.header_title)

        changeMaskColor()
        changeLaserVisibility()
    }

    private fun initListener() {
        var flash = false
        bind.switchFlashlight.setOnClickListener {
            flash = if (flash) {
                bind.zxingBarcodeScanner.setTorchOff()
                if (flashOff != null) bind.switchFlashlight.setImageBitmap(flashOff)
                else bind.switchFlashlight.setImageResource(R.drawable.ic_flash_off)
                false
            } else {
                bind.zxingBarcodeScanner.setTorchOn()
                if (flashOn != null) bind.switchFlashlight.setImageBitmap(flashOn)
                else bind.switchFlashlight.setImageResource(R.drawable.ic_flash_on)
                true
            }
        }

        bind.switchGallery.setOnClickListener {
            openGalleryForImage()
        }
    }

    override fun onResume() {
        super.onResume()
        capture!!.onResume()
    }

    override fun onPause() {
        super.onPause()
        capture!!.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        capture!!.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        capture!!.onSaveInstanceState(outState)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return bind.zxingBarcodeScanner.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event)
    }

    private fun hasFlash(): Boolean {
        return applicationContext.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)
    }

    private fun changeMaskColor(color: Int = Color.argb(100, 0, 0, 0)) {
        bind.zxingBarcodeScanner.viewFinder.setMaskColor(color)
    }

    private fun changeLaserVisibility(visible: Boolean = false) {
        bind.zxingBarcodeScanner.viewFinder.setLaserVisibility(visible)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        capture!!.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun openGalleryForImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_PICK
        openGalleryRequest.launch(intent)
    }

    private val openGalleryRequest =
        registerForActivityResult(androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                it.data?.data?.let { uri -> handleImage(uri) }
            }
        }

    private fun handleImage(uri: Uri) {
        try {
            val image = if (Build.VERSION.SDK_INT < 28) {
                MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
            } else {
                val source = ImageDecoder.createSource(this.contentResolver, uri)
                ImageDecoder.decodeBitmap(source).copy(Bitmap.Config.RGBA_F16, true)
            }

            val intArray = IntArray(image.width * image.height)
            image.getPixels(intArray, 0, image.width, 0, 0, image.width, image.height)

            val source = RGBLuminanceSource(image.width, image.height, intArray)
            val reader = MixedDecoder(MultiFormatReader())
            var result = reader.decode(source)
            if (result == null) {
                result = reader.decode(source)
            }

            val intent = CaptureManager.resultIntent(BarcodeResult(result, null), null)
            setResult(RESULT_OK, intent)
            finish()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}