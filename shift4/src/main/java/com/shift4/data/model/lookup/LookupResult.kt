package com.shift4.data.model.lookup

import com.google.gson.annotations.SerializedName

public data class LookupResult (
    @SerializedName("card")
    val card: Card?,
    @SerializedName("phone")
    val phone: Phone?
)