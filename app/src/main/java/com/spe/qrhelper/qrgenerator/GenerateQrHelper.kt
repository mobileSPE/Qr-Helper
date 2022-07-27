package com.spe.qrhelper.qrgenerator

import android.content.Context
import android.graphics.Bitmap
import com.spe.qrhelper.qrcpm.Samples
import com.spe.qrhelper.model.Data
import com.spe.qrhelper.qrgenerator.core.Core


/**
 * Created by Wildan Nafian on 06/07/2022.
 * Github https://github.com/Wildanafian
 * wildanafian8@gmail.com
 */
class GenerateQrHelper {

    /**
     * Generate qr code from any string
     *
     * @param data  Qr string to be generated to bitmap
     *
     * @return Bitmap format if successfully decoded, null if failed
     *
     * @sample Samples.generate
     */
    fun generate(data: String): Bitmap? = Core().generate(data)

    /**
     * Save bitmap or view to device
     *
     * @param context  Context
     * @param data  The image data to be saved. Use Data.BITMAP(data) if data in bitmap format, use Data.VIEW(data) if data is View
     * @param result  Callback to get save status
     *
     *
     * @return true if successfully, false if failed
     *
     * @sample Samples.saveQrDefault
     *
     * @see Data.BITMAP
     * @see Data.VIEW
     */
    fun save(context: Context, data: Data, result: ((Boolean) -> Unit)? = null) {
        val status = Core().save(context, data)
        result?.invoke(status)
    }

    /**
     * Save bitmap or view to device with custom file name prefix
     *
     * @param context  Context
     * @param data  The image data to be saved. Use Data.BITMAP(data) if data in bitmap format, use Data.VIEW(data) if data is View
     * @param fileNamePrefix  File name prefix, the default name is "img_spe_" and the rest will be followed by "yyyymmdd-hhmmss.jpeg"
     * @param result  Callback to get save status
     *
     *
     * @return true if successfully, false if failed
     *
     * @sample Samples.saveQrWithFilePrefix
     *
     * @see Data.BITMAP
     * @see Data.VIEW
     */
    fun save(context: Context, data: Data, fileNamePrefix: String, result: ((Boolean) -> Unit)? = null) {
        val status = Core().save(context, data, fileNamePrefix)
        result?.invoke(status)
    }

    /**
     * Save bitmap or view to device with custom file name prefix and folder name
     *
     * @param context  Context
     * @param data  The image data to be saved. Use Data.BITMAP(data) if data in bitmap format, use Data.VIEW(data) if data is View
     * @param fileNamePrefix  File name prefix, the default name is "img_spe_" and the rest will be followed by "yyyymmdd-hhmmss.jpeg"
     * @param directoryName  Folder name to save image, the default is in Picture/
     * @param result  Callback to get save status
     *
     *
     * @return true if successfully, false if failed
     *
     * @sample Samples.saveQrWithFilePrefixAndDirectory
     *
     * @see Data.BITMAP
     * @see Data.VIEW
     */
    fun save(context: Context, data: Data, fileNamePrefix: String, directoryName: String, result: ((Boolean) -> Unit)? = null){
        val status = Core().save(context, data, fileNamePrefix, directoryName)
        result?.invoke(status)
    }
}

