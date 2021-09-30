package com.technzone.miniborsa.ui.core.chat.viewimage

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityOptionsCompat
import com.technzone.miniborsa.R
import com.technzone.miniborsa.data.common.Constants
import com.technzone.miniborsa.databinding.ActivityViewImageBinding
import com.technzone.miniborsa.ui.base.activity.BaseBindingActivity
import com.technzone.miniborsa.ui.base.activity.BaseChatActivity
import com.twilio.chat.CallbackListener
import com.twilio.chat.Message
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ViewImageActivity : BaseBindingActivity<ActivityViewImageBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(
            R.layout.activity_view_image,hasToolbar = false
        )
        binding?.data = intent.getStringExtra(Constants.BundleData.IMAGE)
    }

    companion object {
        fun start(
            context: Activity,
            item: Message.Media,
            image: View
        ) {
            val p1: androidx.core.util.Pair<View, String> = androidx.core.util.Pair(
                image,
                image.transitionName
            )
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                context,
                p1
            )
            item.getContentTemporaryUrl(object : CallbackListener<String>() {
                override fun onSuccess(p0: String?) {
                    val intent = Intent(context, ViewImageActivity::class.java)
                    intent.putExtra(Constants.BundleData.IMAGE, p0)
                    context?.startActivity(intent, options.toBundle())
                }
            })

        }
    }



}