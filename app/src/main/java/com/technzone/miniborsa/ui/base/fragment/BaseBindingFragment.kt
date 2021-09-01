package com.technzone.miniborsa.ui.base.fragment

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.akexorcist.localizationactivity.core.OnLocaleChangedListener
import com.technzone.miniborsa.R
import com.technzone.miniborsa.data.api.response.ResponseSubErrorsCodeEnum
import com.technzone.miniborsa.ui.base.activity.IBaseBindingActivity
import com.technzone.miniborsa.ui.base.dialogs.CustomDialogUtils
import com.technzone.miniborsa.ui.base.dialogs.progressdialog.ProgressDialogUtil
import com.technzone.miniborsa.utils.HandleRequestFailedUtil
import com.technzone.miniborsa.utils.extensions.longToast
import com.technzone.miniborsa.utils.extensions.refreshLocal
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

abstract class BaseBindingFragment<BINDING : ViewDataBinding> : Fragment(),
    IBaseBindingFragment, OnLocaleChangedListener {

    protected var binding: BINDING? = null
    protected lateinit var navigationController: NavController

    var rootView: View? = null
    private var shouldCallVisibleView = true
    open fun refreshData() {}
    lateinit var customDialogUtils: CustomDialogUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        activity?.refreshLocal()
        customDialogUtils =
            CustomDialogUtils(requireActivity(), withProgress = true, isShowNow = false)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (rootView == null || shouldRefreshPageWhenReturn()) {
            when {
                binding != null -> {
                    rootView = binding?.root
                }
                isBindingEnabled() -> {
                    binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
                    binding?.lifecycleOwner = this
                    rootView = binding?.root
                }
                else -> {
                    rootView = inflater.inflate(getLayoutId(), container, false)
                }
            }
            shouldCallVisibleView = true
        } else {
            shouldCallVisibleView = false
        }
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (shouldCallVisibleView) {
            onViewVisible()
        }
        if (hasNavigation())
            try {
                navigationController = Navigation.findNavController(view)
            } catch (e: Exception) {

            }
    }


    override fun startActivity(intent: Intent?) =
        super.startActivity(
            intent, ActivityOptions.makeCustomAnimation(
                context,
                R.anim.slide_in_right, R.anim.slide_out_left
            ).toBundle()
        )


    override fun startActivityForResult(intent: Intent?, requestCode: Int) =
        super.startActivityForResult(
            intent, requestCode, ActivityOptions.makeCustomAnimation(
                context,
                R.anim.slide_in_right, R.anim.slide_out_left
            ).toBundle()
        )

    override fun showLoadingView() {
        customDialogUtils.showProgress()
    }

    override fun hideLoadingView() {
        customDialogUtils.hideProgress()
    }

    override fun showProgressDialog(title: String, message: String, isRemovable: Boolean) {
        activity?.let {
            ProgressDialogUtil.showProgressDialog(it, title, message, isRemovable)
        }
    }

    override fun handleRequestFailedMessages(
        errorCode: Int?,
        subErrorCode: ResponseSubErrorsCodeEnum?,
        requestMessage: String?
    ) {
        activity?.let {
            HandleRequestFailedUtil.handleRequestFailedMessages(
                it,
                subErrorCode,
                requestMessage
            )
        }
    }

    override fun addToolbar(
        toolbarView: Toolbar?,
        hasToolbar: Boolean,
        hasBackButton: Boolean,
        hasTitle: Boolean,
        title: Int,
        titleString: String?,
        hasSubTitle: Boolean,
        subTitle: Int,
        showBackArrow: Boolean,
        navigationIcon: Int?
    ) {
        if (activity is IBaseBindingActivity) {
            (activity as IBaseBindingActivity).addToolbar(
                toolbarView = toolbarView,
                hasToolbar = hasToolbar,
                hasBackButton = hasBackButton,
                hasTitle = hasTitle,
                title = title,
                titleString = titleString,
                hasSubTitle = hasSubTitle,
                subTitle = subTitle,
                showBackArrow = showBackArrow,
                navigationIcon = navigationIcon
            )
        }
    }

    override fun updateToolbarTitle(hasTitle: Boolean, title: Int, titleString: String?) {
        if (activity is IBaseBindingActivity) {
            (activity as IBaseBindingActivity).updateToolbarTitle(
                hasTitle, title, titleString
            )
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
        if (activity is IBaseBindingActivity) {
            (activity as IBaseBindingActivity).updateToolbarTitle(
                hasTitle, title, titleString, hasBackButton, backArrowTint, showBackArrow
            )
        }
    }

    override fun updateToolbarSubTitle(
        hasSubTitle: Boolean,
        subTitle: Int,
        subTitleString: String?
    ) {
        if (activity is IBaseBindingActivity) {
            (activity as IBaseBindingActivity).updateToolbarSubTitle(
                hasSubTitle, subTitle, subTitleString
            )
        }
    }

    override fun onAfterLocaleChanged() {

    }

    override fun onBeforeLocaleChanged() {

    }

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

    override fun onDestroy() {
        super.onDestroy()
    }
}