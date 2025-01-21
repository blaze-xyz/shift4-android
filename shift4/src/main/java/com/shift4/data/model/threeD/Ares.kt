package com.shift4.data.model.threeD

import com.google.gson.annotations.SerializedName

public data class Ares(
    @SerializedName("messageVersion")
    val messageVersion: String,
    @SerializedName("clientAuthResponse")
    val clientAuthResponse: String,
    @SerializedName("transStatus")
    val transStatus: String
)