package com.spe.qrhelper.common

import org.json.JSONObject


/**
 * Created by Wildan Nafian on 15/07/2022.
 * Github https://github.com/Wildanafian
 * wildanafian8@gmail.com
 */
interface CommonImpl {

    fun decodeQr(data: String): JSONObject

}