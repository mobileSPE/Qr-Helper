package com.spe.qrhelper.qris.decoder

import com.google.gson.Gson
import com.spe.qrhelper.model.ResultQris
import com.spe.qrhelper.qris.model.QrData


/**
 * Created by Wildan Nafian on 06/07/2022.
 * Github https://github.com/Wildanafian
 * wildanafian8@gmail.com
 */
internal class Core {

    companion object {
        private val helper = QrDecoderImpl()
        private val gson = Gson()
    }

    fun doDecode(data: String): ResultQris {
        val rawJson = helper.decodeQr(data)
        val model = gson.fromJson(rawJson.toString(), QrData::class.java)
        return ResultQris(model, rawJson)
    }

}