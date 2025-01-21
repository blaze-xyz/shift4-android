package com.shift4.data.model.lookup

import com.google.gson.annotations.SerializedName

public data class Phone(
    @SerializedName("number")
    val number: String,
    @SerializedName("verified")
    val verified: Boolean
)