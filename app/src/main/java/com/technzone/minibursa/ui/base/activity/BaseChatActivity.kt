package com.technzone.minibursa.ui.base.activity

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.akexorcist.localizationactivity.core.LocalizationActivityDelegate
import com.akexorcist.localizationactivity.ui.LocalizationActivity
import com.technzone.minibursa.R
import com.technzone.minibursa.data.api.response.ResponseSubErrorsCodeEnum
import com.technzone.minibursa.utils.HandleRequestFailedUtil
import org.jetbrains.anko.longToast
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

abstract class BaseChatActivity<BINDING : ViewDataBinding> : LocalizationActivity(),
    IBaseBindingActivity {

    var binding: BINDING? = null

    private val localizationDelegate = LocalizationActivityDelegate(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        localizationDelegate.addOnLocaleChangedListener(this)
        localizationDelegate.onCreate()
        super.onCreate(savedInstanceState)
    }

    open fun setContentView(
        layoutResID: Int,
        hasToolbar: Boolean = false
    ) {
        if (isBindingEnabled()) {
            binding = DataBindingUtil.inflate(layoutInflater, layoutResID, null, false)
            //To use a LiveData object with your binding class,
            // you need to specify a lifecycle owner to define the scope of the LiveData object.
            binding?.lifecycleOwner = this
            super.setContentView(binding?.root)
        } else {
            super.setContentView(layoutResID)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun handleRequestFailedMessages(
        errorCode: Int?,
        subErrorCode: ResponseSubErrorsCodeEnum?,
        requestMessage: String?
    ) {
        this.let {
            HandleRequestFailedUtil.handleRequestFailedMessages(
                it,
                subErrorCode,
                requestMessage
            )
        }
    }

    override fun startActivity(intent: Intent?) =
        super.startActivity(
            intent, ActivityOptions.makeCustomAnimation(
                this,
                R.anim.slide_in_end, R.anim.slide_out_left
            ).toBundle()
        )


    override fun startActivityForResult(intent: Intent?, requestCode: Int) =
        super.startActivityForResult(
            intent, requestCode, ActivityOptions.makeCustomAnimation(
                this,
                R.anim.slide_in_end, R.anim.slide_out_left
            ).toBundle()
        )

    fun handleError(throwable: Throwable?) {
        when (throwable) {
            is IOException -> {
                longToast(getString(R.string.error_no_internet))
            }
            is SocketTimeoutException -> {
                longToast(getString(R.string.error_server))
            }
            is HttpException -> {

            }
            else -> {
                longToast(getString(R.string.error_msg))
            }
        }
    }

    override fun onNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        window.decorView.layoutDirection =
            if (super.getCurrentLanguage()
                    .toString() == "ar"
            ) View.LAYOUT_DIRECTION_RTL else View.LAYOUT_DIRECTION_LTR
    }

}