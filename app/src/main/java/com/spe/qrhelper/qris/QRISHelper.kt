package com.spe.qrhelper.qris

import com.spe.qrhelper.qrcpm.Samples
import com.spe.qrhelper.qris.decoder.Core
import com.spe.qrhelper.qris.model.QrData
import com.spe.qrhelper.qris.model.QrisDataRawTag
import org.json.JSONObject


/**
 * Created by Wildan Nafian on 06/07/2022.
 * Github https://github.com/Wildanafian
 * wildanafian8@gmail.com
 */
class QRISHelper {

    /**
     * Decode qris string to readable and useable format
     *
     * @param data  Qr string to decode
     * @param result    Callback to get the decoded string result
     *
     * @return model and json format if successfully decoded. if failed will return
     *  { "isValid" : false,
     *    "data" : {}
     *  }
     *
     *  @sample Samples.qrisDecode
     *
     *  @see QrData for data model
     *  @see QrisDataRawTag
     *
     */
    fun decode(data: String, result: (QrData, JSONObject) -> Unit) {
        with(Core().doDecode(data)){
            result.invoke(this.model, this.rawJson)
        }
    }

}