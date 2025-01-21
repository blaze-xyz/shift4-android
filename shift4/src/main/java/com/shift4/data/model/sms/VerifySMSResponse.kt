package com.shift4.data.model.sms

import com.google.gson.annotations.SerializedName
import com.shift4.data.model.lookup.Card

public data class VerifySMSResponse(    @SerializedName("card") val card: Card)