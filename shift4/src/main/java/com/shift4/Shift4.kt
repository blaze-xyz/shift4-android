package com.shift4

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.annotation.DrawableRes
import androidx.annotation.Keep
import androidx.appcompat.app.AppCompatActivity
import com.shift4.checkout.CheckoutDialogFragment
import com.shift4.checkout.TransparentActivity
import com.shift4.data.model.result.Status
import com.shift4.data.model.error.APIError
import com.shift4.data.model.pay.CheckoutRequest
import com.shift4.data.model.result.CheckoutResult
import com.shift4.data.repository.SDKRepository
import com.shift4.threed.ThreeDAuthenticator
import com.shift4.utils.EmailStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel


class Shift4(
    context: Context,
    var publicKey: String,
    var packageName: String,
    private val trustedAppStores: List<String>? = null
) {
    @Keep
    companion object {
        @Keep const val SHIFT4_RESULT_CODE = 7080
    }

    internal val repository = SDKRepository(this)
    internal val emailStorage = EmailStorage(context)
    private var threeDAuthenticator: ThreeDAuthenticator? = null
    private val coroutineScope = CoroutineScope(Job())

    interface CheckoutDialogFragmentResultListener {
        fun onCheckoutFinish(result: CheckoutResult?)
    }

    fun <T> showCheckoutDialog(
        activity: T,
        checkoutRequest: CheckoutRequest,
        merchantName: String,
        description: String,
        collectShippingAddress: Boolean = false,
        collectBillingAddress: Boolean = false,
        email: String? = null,
        @DrawableRes merchantLogo: Int? = null
    ) where T : Activity, T : CheckoutDialogFragmentResultListener {
        if (!checkoutRequest.correct) {
            activity.onCheckoutFinish(
                CheckoutResult(
                    null,
                    APIError.invalidCheckoutRequest
                )
            )
            return
        }

        if (checkoutRequest.termsAndConditions != null) {
            activity.onCheckoutFinish(
                CheckoutResult(
                    null,
                    APIError.unsupportedValue("termsAndConditions")
                )
            )
            return
        }
        if (checkoutRequest.crossSaleOfferIds != null) {
            activity.onCheckoutFinish(
                CheckoutResult(
                    null,
                    APIError.unsupportedValue("crossSaleOfferIds")
                )
            )
            return
        }

        val arguments = Bundle().apply {
            putString("checkoutRequest", checkoutRequest.content)
            putString("publicKey", publicKey)
            putString("packageName", packageName)
            putString("merchantName", merchantName)
            putString("description", description)
            putBoolean("collectShippingAddress", collectShippingAddress)
            putBoolean("collectBillingAddress", collectBillingAddress)
            merchantLogo?.let { putInt("merchantLogoRes", it) }
            email?.let {
                if (it.isNotEmpty()) {
                    putString("initialEmail", it)
                }
            }
            trustedAppStores?.let { putStringArray("trustedAppStores", it.toTypedArray()) }
        }

        if (activity is AppCompatActivity) {
            val checkoutDialogFragment = CheckoutDialogFragment()
            checkoutDialogFragment.arguments = arguments
            checkoutDialogFragment.show(activity.supportFragmentManager, "checkoutDialogFragment")
        } else {
            val intent = Intent(activity, TransparentActivity::class.java)
            intent.apply {
                putExtra("checkoutRequest", checkoutRequest.content)
                putExtra("publicKey", publicKey)
                putExtra("packageName", packageName)
                putExtra("merchantName", merchantName)
                putExtra("description", description)
                putExtra("collectShippingAddress", collectShippingAddress)
                putExtra("collectBillingAddress", collectBillingAddress)
                merchantLogo?.let { putExtra("merchantLogoRes", it) }
                email?.let {
                    if (it.isNotEmpty()) {
                        putExtra("initialEmail", it)
                    }
                }
                trustedAppStores?.let { putExtra("trustedAppStores", it.toTypedArray()) }
            }

            activity.startActivityForResult(intent, 0)
        }
    }

    fun cleanSavedCards() {
        emailStorage.cleanSavedEmails()
    }

    fun cleanUp() {
        coroutineScope.cancel()
    }
}