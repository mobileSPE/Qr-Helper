package com.spe.qrhelper.qrcpm

import android.content.Context
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.spe.qrhelper.model.Data
import com.spe.qrhelper.qrgenerator.GenerateQrHelper
import com.spe.qrhelper.qris.QRISHelper
import com.spe.qrhelper.scan.ScannerHelper


/**
 * Created by Wildan Nafian on 21/07/2022.
 * Github https://github.com/Wildanafian
 * wildanafian8@gmail.com
 */

internal class Samples {

    private lateinit var context: Context
    private lateinit var imageInBitmap: Bitmap
    private lateinit var appCompatActivity: AppCompatActivity
    private lateinit var fragment: Fragment

    fun cpmDecode() {
        QRCPMHelper().decode("your string") { model, raw ->
            val dataInModel = model.data
            val dataInString = raw.toString()
        }
    }

    fun qrisDecode() {
        QRISHelper().decode("your string") { model, raw ->
            val dataInModel = model.data
            val dataInString = raw.toString()
        }
    }

    fun generate() {
        val data = GenerateQrHelper().generate("your string")
    }

    fun saveQrDefault() {
        val data = GenerateQrHelper().save(context, Data.BITMAP(imageInBitmap)) { status ->
            //display some toast
        }
    }

    fun saveQrWithFilePrefix() {
        val data = GenerateQrHelper().save(context, Data.BITMAP(imageInBitmap), "your_prefix_") { status ->
            //display some toast
        }
    }

    fun saveQrWithFilePrefixAndDirectory() {
        val data =
            GenerateQrHelper().save(context, Data.BITMAP(imageInBitmap), "your_prefix_", "your directory") { status ->
                //display some toast
            }
    }

    fun initScanner() {
        ScannerHelper().init(appCompatActivity)
    }

    fun initScannerFragment() {
        ScannerHelper().init(fragment)
    }

    fun displayScannerDefault() {
        ScannerHelper().display { result ->
            //do something with result
        }
    }

    fun autoDecode() {
        ScannerHelper().autoDecode("your string") { raw ->
            val dataInString = raw.toString()
        }
    }
}