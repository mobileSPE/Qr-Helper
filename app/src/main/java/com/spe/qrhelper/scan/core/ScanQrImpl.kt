package com.spe.qrhelper.scan.core

import com.spe.qrhelper.common.CommonImpl
import com.spe.qrhelper.qrcpm.QRCPMHelper
import com.spe.qrhelper.qris.QRISHelper
import org.json.JSONObject


/**
 * Created by Wildan Nafian on 11/07/2022.
 * Github https://github.com/Wildanafian
 * wildanafian8@gmail.com
 */
internal class ScanQrImpl : CommonImpl {

    private val resultData = JSONObject()

    override fun decodeQr(data: String): JSONObject {
        try {
            when {
                data.startsWith("00") -> {
                    QRISHelper().decode(data) { _, rawJson ->
                        if (rawJson.get("isValid").equals(true)) createJson(true, "qris", rawJson.get("data"))
                        else createJson()
                    }
                }
                data.startsWith("hQVDUFY") -> {
                    QRCPMHelper().decode(data) { _, rawJson ->
                        if (rawJson.get("isValid").equals(true)) createJson(true, "qrcpm", rawJson.get("data"))
                        else createJson()
                    }
                }
                else -> {
                    createJson(data = data)
                }
            }
        } catch (e: Exception) {
            createJson()
        }
        return resultData
    }

    private fun createJson(
        validity: Boolean = false,
        qrType: String = "Invalid QR format",
        data: Any = ""
    ) {
        resultData.put("isValid", validity)
        resultData.put("qrType", qrType)
        resultData.put("data", data)
    }

}