package com.technzone.miniborsa.ui.base.activity

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.akexorcist.localizationactivity.core.LocalizationActivityDelegate
import com.akexorcist.localizationactivity.ui.LocalizationActivity
import com.technzone.miniborsa.R
import com.technzone.miniborsa.data.api.response.ResponseSubErrorsCodeEnum
import com.technzone.miniborsa.ui.base.dialogs.CustomDialogUtils
import com.technzone.miniborsa.utils.HandleRequestFailedUtil
import com.technzone.miniborsa.utils.extensions.longToast
import com.technzone.miniborsa.utils.pref.SharedPreferencesUtil
import kotlinx.android.synthetic.main.layout_toolbar.view.*
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

abstract class BaseBindingActivity<BINDING : ViewDataBinding> : LocalizationActivity(),
    IBaseBindingActivity {

    var binding: BINDING? = null
    private var toolbar: Toolbar? = null


    lateinit var customDialogUtils: CustomDialogUtils
    override fun showLoadingView() {
        customDialogUtils.showProgress()
    }

    override fun hideLoadingView() {
        customDialogUtils.hideProgress()
    }
    private val localizationDelegate = LocalizationActivityDelegate(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        localizationDelegate.addOnLocaleChangedListener(this)
        localizationDelegate.onCreate()
        customDialogUtils = CustomDialogUtils(this, withProgress = true, isShowNow = false)
        super.onCreate(savedInstanceState)
    }

    open fun setContentView(
        layoutResID: Int,
        hasToolbar: Boolean = false,
        hasBackButton: Boolean = false,
        toolbarView: Toolbar? = null,
        backArrowTint: Int? = null,
        hasTitle: Boolean = false,
        title: Int = R.string.empty_string,
        titleString: String? = null,
        hasSubTitle: Boolean = false,
        subTitle: Int = R.string.empty_string,
        showBackArrow: Boolean = false,
        @DrawableRes navigationIcon: Int? = null
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

        addToolbar(
            hasToolbar,
            toolbarView,
            hasBackButton,
            backArrowTint,
            hasTitle,
            title,
            titleString,
            hasSubTitle,
            subTitle,
            showBackArrow,
            navigationIcon
        )
    }

    override fun addToolbar(
        hasToolbar: Boolean,
        toolbarView: Toolbar?,
        hasBackButton: Boolean,
        backArrowTint: Int?,
        hasTitle: Boolean,
        title: Int,
        titleString: String?,
        hasSubTitle: Boolean,
        subTitle: Int,
        showBackArrow: Boolean,
        navigationIcon: Int?
    ) {
        if (!hasToolbar) return

        toolbar = toolbarView ?: findViewById(R.id.toolbar)

        if (toolbar == null) return

        setSupportActionBar(toolbar)

        if (hasTitle) {
            supportActionBar?.title = ""
            toolbar?.tvTitle?.text =
                if (titleString.isNullOrEmpty()) getString(title) else titleString
        } else {
            supportActionBar?.setDisplayShowTitleEnabled(false)
        }

        if (hasSubTitle) {
            supportActionBar?.subtitle = getString(subTitle)
        }

        if (hasBackButton) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            if (navigationIcon != null) {
                toolbar?.navigationIcon = ContextCompat.getDrawable(this, navigationIcon)
            }
        } else {
            toolbar?.navigationIcon = null
        }

        if (!showBackArrow) {
            toolbar?.navigationIcon = null
        } else {
            if (backArrowTint != null)
                toolbar?.navigationIcon?.setTint(ContextCompat.getColor(this, backArrowTint))
        }
    }

    override fun updateToolbarTitle(
        hasTitle: Boolean,
        @StringRes title: Int,
        titleString: String?
    ) {
        supportActionBar.let {
            if (hasTitle) {
                if (titleString == null) {
                    supportActionBar?.title = getString(title)
                } else {
                    supportActionBar?.title = titleString
                }
            } else {
                supportActionBar?.setDisplayShowTitleEnabled(false)
                supportActionBar?.title = getString(R.string.empty_string)
            }
        }
    }

    override fun updateToolbarTitle(
        hasTitle: Boolean,
        title: Int,
        titleString: String?,
        hasBackButton: Boolean,
        backArrowTint: Int?,
        showBackArrow: Boolean
    ) {

        if (hasTitle) {
            supportActionBar?.title =
                if (titleString.isNullOrEmpty()) getString(title) else titleString
        } else {
            supportActionBar?.setDisplayShowTitleEnabled(false)
        }

        if (hasBackButton) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        } else {
            toolbar?.navigationIcon = null
        }

        if (!showBackArrow) {
            toolbar?.navigationIcon = null
        } else {
            if (backArrowTint != null)
                toolbar?.navigationIcon?.setTint(ContextCompat.getColor(this, backArrowTint))
        }
    }

    override fun updateToolbarSubTitle(
        hasSubTitle: Boolean,
        @StringRes subTitle: Int,
        subTitleString: String?
    ) {
        supportActionBar.let {
            if (hasSubTitle) {
                if (subTitleString == null) {
                    supportActionBar?.subtitle = getString(subTitle)
                } else {
                    supportActionBar?.subtitle = subTitleString
                }
            } else {
                supportActionBar?.subtitle = null
            }
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

    fun showUnAuthorizedDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(getString(R.string.session_ended))
        val dialog: AlertDialog = builder.create()

        builder.setNeutralButton(R.string.ok) { _, _ ->
            dialog.dismiss()
        }

        builder.setOnDismissListener {
            SharedPreferencesUtil.getInstance(this).logout()
//            AuthActivity.start(this)
        }

        builder.show()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        window.decorView.layoutDirection =
            if (super.getCurrentLanguage()
                    .toString() == "ar"
            ) View.LAYOUT_DIRECTION_RTL else View.LAYOUT_DIRECTION_LTR
    }

}