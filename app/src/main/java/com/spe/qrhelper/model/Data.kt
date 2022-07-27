package com.spe.qrhelper.model

import android.graphics.Bitmap
import android.view.View


/**
 * Created by Wildan Nafian on 20/07/2022.
 * Github https://github.com/Wildanafian
 * wildanafian8@gmail.com
 */
sealed class Data {
    class BITMAP(val data: Bitmap) : Data()
    class VIEW(val data: View) : Data()
}
