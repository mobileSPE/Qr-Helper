package com.spe.qrhelper.qrgenerator.core

import android.graphics.Bitmap


/**
 * Created by Wildan Nafian on 10/07/2022.
 * Github https://github.com/Wildanafian
 * wildanafian8@gmail.com
 */
internal interface GenerateQr {

    fun generate(data: String): Bitmap?

}