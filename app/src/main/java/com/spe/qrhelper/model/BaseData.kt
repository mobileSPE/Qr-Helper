package com.spe.qrhelper.model

import com.google.gson.annotations.SerializedName


/**
 * Created by Wildan Nafian on 15/07/2022.
 * Github https://github.com/Wildanafian
 * wildanafian8@gmail.com
 */
data class BaseData<T>(
    @field:SerializedName("qrType")
    val qrType: String,

    @field:SerializedName("data")
    val data: T,

    @field:SerializedName("isValid")
    val isValid: Boolean
)
