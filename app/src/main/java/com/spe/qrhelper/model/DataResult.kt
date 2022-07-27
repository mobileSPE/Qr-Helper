package com.spe.qrhelper.model

import com.spe.qrhelper.qrcpm.model.QrCPMData
import com.spe.qrhelper.qris.model.QrData
import org.json.JSONObject


/**
 * Created by Wildan Nafian on 19/07/2022.
 * Github https://github.com/Wildanafian
 * wildanafian8@gmail.com
 */
data class ResultQrCPM(val model: QrCPMData, val rawJson: JSONObject)

data class ResultQris(val model: QrData, val rawJson: JSONObject)

