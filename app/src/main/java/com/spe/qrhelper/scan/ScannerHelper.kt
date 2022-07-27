package com.spe.qrhelper.scan

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.journeyapps.barcodescanner.ScanOptions
import com.spe.qrhelper.model.BaseData
import com.spe.qrhelper.qrcpm.Samples
import com.spe.qrhelper.qrcpm.model.QrCPMData
import com.spe.qrhelper.qris.model.QrData
import com.spe.qrhelper.scan.core.Core.autoDecode
import com.spe.qrhelper.scan.core.Core.displayScanner
import com.spe.qrhelper.scan.core.Core.displayScannerFragment
import com.spe.qrhelper.scan.core.Core.initializeScanner
import com.spe.qrhelper.scan.core.Core.provideLifecycleOwner
import com.spe.qrhelper.scan.core.CustomScannerActivity
import org.json.JSONObject


/**
 * Created by Wildan Nafian on 14/07/2022.
 * Github https://github.com/Wildanafian
 * wildanafian8@gmail.com
 */
class ScannerHelper {

    /**
     * Initialize scanner before use
     *
     * @param context  Context AppCompatActivity
     *
     *  @sample Samples.initScanner
     *
     */
    fun init(context: AppCompatActivity) = initializeScanner(context)

    /**
     * Initialize scanner before use
     *
     * @param context  Context Fragment
     *
     *  @sample Samples.initScannerFragment
     *
     */
    @ExperimentalUnsignedTypes
    fun init(context: Fragment) = initializeScanner(context)

    /**
     * Display Scanner and return decoded string result
     *
     * @param scanOptions  Scanner preference, the default is only support QRCODE,
     * OrientationLocked(true), BeepEnabled(false),Timeout(20000), captureActivity = CustomActivity
     *
     * @return decoded string result
     *
     * @sample Samples.displayScannerDefault
     *
     * @see CustomScannerActivity
     * @see BaseData
     * @see QrCPMData
     * @see QrData
     */
    fun display(scanOptions: ScanOptions? = null, result: (String) -> Unit) {
        displayScanner(scanOptions).observe(provideLifecycleOwner()) {
            result.invoke(it)
        }
    }

    @ExperimentalUnsignedTypes
    fun displayFragment() = displayScannerFragment()


    /**
     * Auto decode qr string into readable and usable data
     *
     * @param data  Qr string
     *
     * @return Json object
     *
     * @sample Samples.autoDecode
     *
     */
    fun autoDecode(data: String, result: (JSONObject) -> Unit) = result.invoke(autoDecode(data))

}