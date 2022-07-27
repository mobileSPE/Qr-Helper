package com.spe.qrhelper.scan.core

import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import org.json.JSONObject


/**
 * Created by Wildan Nafian on 11/07/2022.
 * Github https://github.com/Wildanafian
 * wildanafian8@gmail.com
 */
internal object Core {

    private var core = ScanQrImpl()
    private lateinit var mContext: AppCompatActivity
    private var scanResult_ = MutableLiveData("")
    private var scanResult: LiveData<String> = scanResult_
    private var customActivity: ActivityResultLauncher<ScanOptions>? = null

    fun initializeScanner(context: AppCompatActivity) {
        try {
            mContext = context
            customActivity = mContext.registerForActivityResult(ScanContract()) { result: ScanIntentResult ->
                    scanResult_.postValue(if (result.contents == null) "" else result.contents.decodeStringResult())
                }
        } catch (e: Exception) {
            throw e
        }
    }

    fun displayScanner(
        options: ScanOptions?
    ): LiveData<String> {
        return try {
            val scanOptions = options ?: ScanOptions().apply {
                    captureActivity = CustomScannerActivity::class.java
                    setDesiredBarcodeFormats(ScanOptions.QR_CODE)
                    setOrientationLocked(true)
                    setBeepEnabled(false)
                    setTimeout(20000)
                }

            customActivity!!.launch(scanOptions)
            scanResult
        } catch (e: Exception) {
            scanResult_.postValue("")
            scanResult
            throw e
        }
    }

    fun initializeScanner(context: Fragment) {
        try {
            customActivity =
                context.registerForActivityResult(ScanContract()) { result: ScanIntentResult ->
                    scanResult_.postValue(
                        if (result.contents == null) "" else result.contents.decodeStringResult()
                    )
                }
        } catch (e: Exception) {
            throw e
        }
    }

    fun displayScannerFragment(
        scanOptions: ScanOptions = ScanOptions().apply {
            setDesiredBarcodeFormats(ScanOptions.QR_CODE)
            setOrientationLocked(true)
            setBeepEnabled(false)
            setTimeout(20000)
            captureActivity = CustomScannerFragment::class.java
        }
    ) {
        try {
            customActivity!!.launch(scanOptions)
        } catch (e: Exception) {
            throw e
        }
    }

    internal fun provideLifecycleOwner() = mContext

    private fun String.decodeStringResult(): String = core.decodeQr(this).toString()

    fun autoDecode(data: String): JSONObject = core.decodeQr(data)

}