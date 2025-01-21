package com.shift4.data.model.checkoutRequestDetails

import com.google.gson.annotations.SerializedName
import com.shift4.data.model.subscription.Subscription

public data class CheckoutRequestDetails(
    @SerializedName("sessionId")
    val sessionId: String,
    @SerializedName("threeDSecureRequired")
    val threeDSecureRequired: Boolean,
    @SerializedName("subscription")
    val subscription: Subscription?
)