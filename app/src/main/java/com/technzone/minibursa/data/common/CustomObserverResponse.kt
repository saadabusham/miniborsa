package com.technzone.minibursa.data.common

import android.app.Activity
import androidx.lifecycle.Observer
import com.technzone.minibursa.data.api.response.APIResource
import com.technzone.minibursa.data.api.response.RequestStatusEnum
import com.technzone.minibursa.data.api.response.ResponseSubErrorsCodeEnum
import com.technzone.minibursa.data.api.response.ResponseWrapper
import com.technzone.minibursa.ui.base.dialogs.CustomDialogUtils
import com.technzone.minibursa.utils.HandleRequestFailedUtil

class CustomObserverResponse<T>(
    private val activity: Activity,
    private val apiCallBack: APICallBack<T>,
    private val withProgress: Boolean = true,
    private val showError: Boolean = true
) : CustomDialogUtils(activity, withProgress, false),
    Observer<APIResource<ResponseWrapper<T>>> {

    override fun onChanged(responseWrapperResponse: APIResource<ResponseWrapper<T>>) {
        when (responseWrapperResponse.status) {
            RequestStatusEnum.SUCCESS -> {
                if (withProgress) {
                    hideProgress()
                }
                if (responseWrapperResponse.statusSubCode == ResponseSubErrorsCodeEnum.Success) {
                    apiCallBack.onSuccess(
                        responseWrapperResponse.statusCode
                            ?: -1,
                        responseWrapperResponse.statusSubCode,
                        responseWrapperResponse.data?.data
                    )
                    apiCallBack.onSuccess(
                        responseWrapperResponse.statusCode
                            ?: -1,
                        responseWrapperResponse.statusSubCode,
                        responseWrapperResponse.data
                    )
                    apiCallBack.onSuccess(
                        responseWrapperResponse.statusCode
                            ?: -1, responseWrapperResponse.statusSubCode, responseWrapperResponse
                    )
                } else {
                    if (showError)
                        handleRequestFailedMessages(
                            responseWrapperResponse.statusSubCode,
                            responseWrapperResponse.messages
                        )

                    responseWrapperResponse.messages?.let {
                        responseWrapperResponse.statusSubCode?.let { it1 ->
                            apiCallBack.onError(it1, it)
                        }
                    }
                }
            }
            RequestStatusEnum.FAILED -> {
                if (withProgress) {
                    hideProgress()
                }
                if (responseWrapperResponse.statusSubCode == ResponseSubErrorsCodeEnum.EmailNotVerified) {
                    apiCallBack.onSuccess(
                        responseWrapperResponse.statusCode
                            ?: -1,
                        responseWrapperResponse.statusSubCode,
                        responseWrapperResponse.data?.data
                    )
                    return
                }
                if (showError)
                    handleRequestFailedMessages(
                        responseWrapperResponse.statusSubCode,
                        responseWrapperResponse.messages
                    )
                responseWrapperResponse.messages?.let {
                    responseWrapperResponse.statusSubCode?.let { it1 ->
                        apiCallBack.onError(it1, it)
                    }
                }
            }
            RequestStatusEnum.LOADING -> {
                if (withProgress) {
                    showProgress()
                }
                apiCallBack.onLoading()
            }
            else -> {}
        }
    }

    interface APICallBack<T> {
        fun onSuccess(statusCode: Int, subErrorCode: ResponseSubErrorsCodeEnum, data: T?) {}
        fun onSuccess(
            statusCode: Int,
            subErrorCode: ResponseSubErrorsCodeEnum,
            data: ResponseWrapper<T>?
        ) {
        }

        fun onSuccess(
            statusCode: Int,
            subErrorCode: ResponseSubErrorsCodeEnum,
            data: APIResource<ResponseWrapper<T>>
        ) {
        }

        fun onError(subErrorCode: ResponseSubErrorsCodeEnum, message: String) {}
        fun onLoading() {}
    }

    private fun handleRequestFailedMessages(
        subErrorCode: ResponseSubErrorsCodeEnum?,
        requestMessage: String?
    ) {
        activity.let {
            HandleRequestFailedUtil.handleRequestFailedMessages(
                it,
                subErrorCode,
                requestMessage
            )
        }

    }

}