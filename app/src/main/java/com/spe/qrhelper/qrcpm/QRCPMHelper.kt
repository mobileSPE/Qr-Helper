package com.spe.qrhelper.qrcpm

import com.spe.qrhelper.qrcpm.decoder.Core
import com.spe.qrhelper.qrcpm.model.QrCPMData
import org.json.JSONObject

/**
 * Created by Wildan Nafian on 11/06/2022.
 * Github https://github.com/Wildanafian
 * wildanafian8@gmail.com
 */
class QRCPMHelper {

    /**
     * Decode qr cpm string to readable and useable format
     *
     * @sample Samples.cpmDecode
     *
     * @param data  Qr string to decode
     * @param result    Callback to get the decoded string result
     *
     * @return model and json format if successfully decoded. if failed will return
     *  { "isValid" : false,
     *    "data" : {}
     *  }
     *
     *  @see QrCPMData for data model
     *
     */
    fun decode(data: String, result: (QrCPMData, JSONObject) -> Unit) {
        with(Core().doDecode(data)) {
            result.invoke(this.model, this.rawJson)
        }
    }

}