package com.shift4.data.model.pay

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.shift4.utils.CurrencyFormatter
import java.util.*

data class CheckoutRequest(@SerializedName("content") public val content: String) {
    public val correct: Boolean
        get() {
            return try {
                checkoutRequestContent
                true
            } catch (_: Exception) {
                false
            }
        }

    public val amount: Int
        get() {
            return checkoutRequestContent.charge?.amount ?: 0
        }

    public val currency: String
        get() {
            return checkoutRequestContent.charge?.currency ?: checkoutRequestContent.customCharge?.currency ?: ""
        }

    public val readable: String
        get() {
            val amount = checkoutRequestContent.charge?.amount ?: return ""
            val currency = checkoutRequestContent.charge?.currency ?: return ""
            return CurrencyFormatter.format(
                amount.toBigDecimal(),
                currency,
                divideMinorUnits = true
            )
        }

    public val rememberMe: Boolean
        get() {
            return checkoutRequestContent.rememberMe ?: false
        }

    public val termsAndConditions: String?
        get() {
            return checkoutRequestContent.termsAndConditionsUrl
        }

    public val customerId: String?
        get() {
            return checkoutRequestContent.customerId
        }

    public val crossSaleOfferIds: Array<String>?
        get() {
            return checkoutRequestContent.crossSaleOfferIds
        }

    public val donations: List<Donation>?
        get() {
            if (checkoutRequestContent.customCharge?.amountOptions?.isEmpty() != false) {
                return null
            }
            return checkoutRequestContent.customCharge?.amountOptions?.map {
                Donation(it, checkoutRequestContent.customCharge?.currency ?: "")
            }
        }

    public val customDonation: Pair<Int, Int>?
        get() {
            val min = checkoutRequestContent.customCharge?.customAmount?.min ?: return null
            val max = checkoutRequestContent.customCharge?.customAmount?.max ?: return null
            return Pair(min, max)
        }

    public val threeDSecure: Boolean
        get() {
            return checkoutRequestContent.threeDSecure?.enable ?: false
        }

    public val requireEnrolledCard: Boolean
        get() {
            return checkoutRequestContent.threeDSecure?.requireEnrolledCard ?: false
        }

    public val requireSuccessfulLiabilityShiftForEnrolledCard: Boolean
        get() {
            return checkoutRequestContent.threeDSecure?.requireSuccessfulLiabilityShiftForEnrolledCard ?: false
        }

    public val subscriptionPlanId: String?
        get() {
            return checkoutRequestContent.subscription?.planId
        }

    private val checkoutRequestContent: CheckoutRequestContent
        get() {
            val decoded = String(Base64.getDecoder().decode(content))
            val json = decoded.split("|").last()
            try {
                return Gson().fromJson(json, CheckoutRequestContent::class.java)
            } catch (e: Exception) {
                throw(Exception("Invalid Checkout Request"))
            }
        }
}