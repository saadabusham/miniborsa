package com.technzone.minibursa.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.dynamiclinks.DynamicLink
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.google.firebase.dynamiclinks.ShortDynamicLink
import com.technzone.minibursa.R
import com.technzone.minibursa.ui.base.bindingadapters.getFullUrl

object DeepLinkUtil {

    fun Context.generateDeepLink(
        key: String?,
        value: String,
        description: String? = "",
        image: String? = "",
        socialTag: String? = "",
        shareTitle: String,
        title: String = ""
    ) {
        try {
            val dynamicLink: Task<ShortDynamicLink> =
                FirebaseDynamicLinks.getInstance().createDynamicLink()
                    .setLink(Uri.parse("https://www.minibursa.com/?$key=$value"))
                    .setDomainUriPrefix("https://minibursaapp.page.link")
                    .setAndroidParameters(
                        DynamicLink.AndroidParameters.Builder("com.technzone.minibursa")
                            .setFallbackUrl(Uri.parse("www.minibursa.com"))
                            .build()
                    )
                    .setIosParameters(
                        DynamicLink.IosParameters.Builder("com.technzone.minibursa")
                            .setAppStoreId("123456789")
                            .setFallbackUrl(Uri.parse("www.minibursa.com"))
                            .build()
                    )
                    .setSocialMetaTagParameters(
                        DynamicLink.SocialMetaTagParameters.Builder()
                            .setTitle(
                                socialTag ?: ""
                            )
                            .setDescription(description ?: "")
                            .setImageUrl(
                                Uri.parse(
                                    getFullUrl(
                                        image ?: ""
                                    )
                                )
                            )
                            .build()
                    )
                    .buildShortDynamicLink()
            dynamicLink.addOnCompleteListener { task ->
                if (task.isSuccessful)
                    share(
                        task.result?.shortLink.toString(),
                        shareTitle, title
                    )
            }.toString()
        } catch (e: IllegalStateException) {
            e
        }
    }


    fun Context.share(link: String, shareTitle: String, title: String = "") {
        try {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(
                Intent.EXTRA_SUBJECT,
                resources.getString(R.string.app_name)
            )
            var shareMessage =
                "\n${
                    shareTitle
                } $title ${
                    resources.getString(R.string.share_on_mini_bursa)
                }\n\n"
            shareMessage = "$shareMessage$link\n\n"
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
            startActivity(Intent.createChooser(shareIntent, null))
        } catch (e: Exception) {
            //e.toString();
        }
    }

}