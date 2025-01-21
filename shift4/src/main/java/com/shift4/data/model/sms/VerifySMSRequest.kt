package com.shift4.data.model.sms

import com.google.gson.annotations.SerializedName
import com.shift4.utils.empty

public data class VerifySMSRequest(
    @SerializedName("code")
    private val code: String = String.empty
)