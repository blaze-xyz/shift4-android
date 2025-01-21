package com.shift4.data.model.checkoutRequestDetails

import com.google.gson.annotations.SerializedName

public data class CheckoutDetailsRequest(
    @SerializedName("key")
    val key: String,
    @SerializedName("checkoutRequest")
    val checkoutRequest: String
)