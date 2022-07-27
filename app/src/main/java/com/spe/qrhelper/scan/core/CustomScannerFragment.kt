package com.spe.qrhelper.scan.core

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.zxing.MultiFormatReader
import com.google.zxing.RGBLuminanceSource
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.CaptureManager
import com.journeyapps.barcodescanner.MixedDecoder
import com.spe.qrhelper.R
import com.spe.qrhelper.databinding.ActivityCustomScannerBinding


class CustomScannerFragment : Fragment() {

    private lateinit var bind: ActivityCustomScannerBinding
    private var capture: CaptureManager? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bind = ActivityCustomScannerBinding.inflate(inflater, container, false)

        initView(savedInstanceState)
        initListener()

        return bind.root
    }

    private fun initView(savedInstanceState: Bundle?) {
        bind.zxingBarcodeScanner.statusView.visibility = View.GONE

        if (!hasFlash()) {
            bind.switchFlashlight.visibility = View.GONE
        }

        capture = CaptureManager(requireActivity(), bind.zxingBarcodeScanner)
        capture!!.initializeFromIntent(requireActivity().intent, savedInstanceState)
        capture!!.setShowMissingCameraPermissionDialog(false)
        capture!!.decode()

        changeMaskColor()
        changeLaserVisibility()
    }

    private fun initListener() {
        var flash = false
        bind.switchFlashlight.setOnClickListener {
            flash = if (flash) {
                bind.zxingBarcodeScanner.setTorchOff()
                bind.switchFlashlight.setImageResource(R.drawable.ic_flash_off)
                false
            } else {
                bind.zxingBarcodeScanner.setTorchOn()
                bind.switchFlashlight.setImageResource(R.drawable.ic_flash_on)
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

//    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
//        return bind.zxingBarcodeScanner.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event)
//    }



    private fun hasFlash(): Boolean {
        return requireContext().packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)
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
                MediaStore.Images.Media.getBitmap(requireContext().contentResolver, uri)
            } else {
                val source = ImageDecoder.createSource(requireContext().contentResolver, uri)
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
            requireActivity().setResult(RESULT_OK, intent)
            requireActivity().finish()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}