package com.technzone.miniborsa.data.api.response

import retrofit2.HttpException
import javax.inject.Inject

open class ResponseHandler @Inject constructor() {
    fun <RESPONSE_DATA> handleSuccess(data: RESPONSE_DATA?): APIResource<RESPONSE_DATA> {
        if (data !is ResponseWrapper<*>) {
            return APIResource.success(data)
        }

        if (data.success) {
            return APIResource.success(data, data.message,data.code.toInt())
        }
        return APIResource.error(
            data = data,
            msgs = data.message,
            statusCode = data.code.toInt(),
            failedStatusSubCode = ResponseSubErrorsCodeEnum.getResponseSubErrorsCodeEnumByValue(data.code.toInt())
        )
    }

    fun <RESPONSE_DATA : Any> handleException(e: Exception): APIResource<RESPONSE_DATA> {
        return when (e) {
            is HttpException -> {
                APIResource.error(
                    getErrorMessage(e.code()),
                    null,
                    e.code(),
                    ResponseSubErrorsCodeEnum.GENERAL_FAILED
                )
            }
            else -> APIResource.error(
                getErrorMessage(Int.MAX_VALUE),
                null,
                -1,
                ResponseSubErrorsCodeEnum.GENERAL_FAILED
            )
        }
    }

    private fun getErrorMessage(code: Int): String {
        return when (code) {
            401 -> "Unauthorised"
            404 -> "Not found"
            else -> "Please Try Again Later"
        }
    }
}