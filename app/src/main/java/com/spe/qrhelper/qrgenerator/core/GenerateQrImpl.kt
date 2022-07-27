package com.spe.qrhelper.qrgenerator.core

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.spe.qrhelper.model.Data
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by Wildan Nafian on 10/07/2022.
 * Github https://github.com/Wildanafian
 * wildanafian8@gmail.com
 */
internal class GenerateQrImpl : GenerateQr {

    override fun generate(data: String): Bitmap? {
        return try {
            val bitMatrix = MultiFormatWriter().encode(data.cleanString(), BarcodeFormat.QR_CODE, 800, 800)
            BarcodeEncoder().createBitmap(bitMatrix)
        } catch (e: Exception) {
            null
        }
    }

    fun save(
        context: Context,
        data: Data,
        fileNamePrefix: String = "img_spe_",
        directoryName: String = "Pictures"
    ): Boolean {
        return try {
            val fileName = fileNamePrefix.createFileName()
            when (data) {
                is Data.BITMAP -> saveToDevice(context, fileName, data.data, directoryName)
                is Data.VIEW -> data.data.convertToBitmap()?.let { saveToDevice(context, fileName, it, directoryName) } ?: false
            }
        } catch (e: Exception) {
            false
        }
    }

    private fun saveToDevice(
        context: Context,
        fileName: String,
        image: Bitmap,
        directoryName: String
    ): Boolean {
        if (context.checkPermission()) {
            var saveStatus = false
            val values = ContentValues()
            val contentResolver = context.contentResolver
            if (Build.VERSION.SDK_INT >= 29) {
                values.put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
                values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                values.put(MediaStore.Images.Media.RELATIVE_PATH, directoryName)

                val uri = contentResolver.insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    values
                )
                if (uri != null) {
                    saveStatus = image.saveImageToStream(contentResolver.openOutputStream(uri))
                    contentResolver.update(uri, values, null, null)
                }
            } else {
                val directory = directoryName.prepareDirectory()
                if (directory.checkDirectoryIsNotExists()) directory.mkdirs()
                val file = File(directory, fileName)
                saveStatus = image.saveImageToStream(FileOutputStream(file))
                values.put(MediaStore.Images.Media.DATA, file.absolutePath)
                contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            }
            return saveStatus
        } else {
            context.requestPermission()
            return false
        }
    }

    private fun String.cleanString() = this.replace("\n", "")

    private fun Context.checkPermission(): Boolean =
        ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED

    private fun View.convertToBitmap(): Bitmap? {
        val bitmap = Bitmap.createBitmap(this.width, this.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        this.draw(canvas)
        return bitmap
    }

    private fun String.prepareDirectory() =
        File(Environment.getExternalStorageDirectory(), this)

    private fun File.checkDirectoryIsNotExists() = !this.exists()

    private fun Context.requestPermission() {
        ActivityCompat.requestPermissions(
            this as Activity,
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
            1
        )
    }

    private fun Bitmap.saveImageToStream(outputStream: OutputStream?): Boolean = try {
        val status = this.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        outputStream?.close()
        status
    } catch (e: Exception) {
        false
    }

    private fun String.createFileName() = this+getCurrentTime() + ".jpeg"

    private fun getCurrentTime(): String = try {
        SimpleDateFormat(
            "yyyyMMdd-HHmmss",
            Locale("id", "ID")
        ).format(Calendar.getInstance().time)
    } catch (e: Exception) {
        "19450817-112233"
    }

}