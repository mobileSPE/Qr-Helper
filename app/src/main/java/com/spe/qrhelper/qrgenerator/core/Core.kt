package com.spe.qrhelper.qrgenerator.core

import android.content.Context
import android.graphics.Bitmap
import com.spe.qrhelper.model.Data


/**
 * Created by Wildan Nafian on 10/07/2022.
 * Github https://github.com/Wildanafian
 * wildanafian8@gmail.com
 */
internal class Core {

    companion object {
        private val helper = GenerateQrImpl()
    }

    fun generate(data: String): Bitmap? = helper.generate(data)

    fun save(context: Context, data: Data) = helper.save(context, data)

    fun save(context: Context, data: Data, fileNamePrefix: String) =
        helper.save(context, data, fileNamePrefix)

    fun save(context: Context, image: Data, fileNamePrefix: String, directoryName: String) =
        helper.save(context, image, fileNamePrefix, directoryName)

}