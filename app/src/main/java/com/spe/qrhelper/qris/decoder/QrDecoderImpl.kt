package com.spe.qrhelper.qris.decoder

import com.spe.qrhelper.common.CommonImpl
import org.json.JSONObject


/**
 * Created by Wildan Nafian on 06/07/2022.
 * Github https://github.com/Wildanafian
 * wildanafian8@gmail.com
 */
internal class QrDecoderImpl : CommonImpl {

    private val resultData = JSONObject()

    override fun decodeQr(data: String): JSONObject {
        if (data.checkValidity()) {

            var qrString = data.cleanUnnecessaryCharacter()
            val tempJsonData = JSONObject()
            var i = 0

            while (qrString.isNotEmpty() && i < 100) {
                val tag = qrString.getTag()
                val valueLength = qrString.getTagLengthValue()
                val value = qrString.getValueFromTagLength(valueLength)
                qrString = qrString.removeValue(4 + valueLength)

                tempJsonData.put(tag, value.checkSubValue(tag))
                i++
            }
            predefineJson(true, tempJsonData)
        } else {
            predefineJson()
        }
        return resultData
    }

    private fun predefineJson(validity: Boolean = false, data: Any = "") {
        resultData.put("isValid", validity)
        resultData.put("data", data)
    }

    private fun String.checkValidity(): Boolean = this.startsWith("00", true)

    private fun String.cleanUnnecessaryCharacter(): String =
        if (this.takeLast(2) == "==") this.dropLast(2).replace("\n", "") else this.replace("\n", "")

    private fun String.removeValue(count: Int) = this.drop(count)

    private fun String.getTag() = this.take(2)

    private fun String.getTagLengthValue() = this.substring(2, 4).toDouble().toInt()

    private fun String.getValueFromTagLength(count: Int) =
        this.substring(4, 4 + count)

    private fun String.checkSubValue(tag: String): Any =
        if (tag.hasSubValue()) this.getSubValue() else this

    private fun String.hasSubValue(): Boolean =
        this.toDouble().toInt() in 26..51 || this.toDouble().toInt() == 62

    private fun String.getSubValue(): JSONObject {
        val tempResultData = JSONObject()
        var qrString = this
        var i = 0
        tempResultData.put("raw", this)
        while (qrString.isNotEmpty() && i < 5) {
            val tag = qrString.getTag()
            val valueLength = qrString.getTagLengthValue()
            val value = qrString.getValueFromTagLength(valueLength)
            qrString = qrString.removeValue(4 + valueLength)
            tempResultData.put(tag, value)
            i++
        }
        return tempResultData
    }

}