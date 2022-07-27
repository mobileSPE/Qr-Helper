package com.spe.qrhelper.qrcpm.decoder

import com.google.gson.Gson
import com.spe.qrhelper.model.ResultQrCPM
import com.spe.qrhelper.qrcpm.model.QrCPMData


/**
 * Created by Wildan Nafian on 11/06/2022.
 * Github https://github.com/Wildanafian
 * wildanafian8@gmail.com
 */
internal class Core {

    companion object {
        private val helper = QrDecoderImpl()
        private val gson = Gson()
    }

    fun doDecode(data: String): ResultQrCPM {
        val rawJson = helper.decodeQr(data)
        val model = gson.fromJson(rawJson.toString(), QrCPMData::class.java)
        return ResultQrCPM(model, rawJson)
    }

}