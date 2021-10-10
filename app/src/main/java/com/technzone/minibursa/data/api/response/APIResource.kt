package com.technzone.minibursa.data.api.response


data class APIResource<out RESPONSE_DATA>(
    val status: RequestStatusEnum,
    val data: RESPONSE_DATA?,
    val messages: String?,
    val statusCode: Int?,
    val statusSubCode: ResponseSubErrorsCodeEnum?
) {

    companion object {
        fun <RESPONSE_DATA> success(data: RESPONSE_DATA?  , messages: String?=null  ,statusCode: Int?=null  ): APIResource<RESPONSE_DATA> {
            return APIResource(
                RequestStatusEnum.SUCCESS,
                data,
                messages,
                statusCode,
                ResponseSubErrorsCodeEnum.Success
            )
        }

        fun <RESPONSE_DATA> error(
            msgs: String,
            data: RESPONSE_DATA?,
            statusCode: Int,
            failedStatusSubCode: ResponseSubErrorsCodeEnum
        ): APIResource<RESPONSE_DATA> {
            return APIResource(
                RequestStatusEnum.FAILED,
                data,
                msgs,
                statusCode,
                failedStatusSubCode
            )
        }

        fun <RESPONSE_DATA> loading(): APIResource<RESPONSE_DATA> {
            return APIResource(RequestStatusEnum.LOADING, null, null, null, null)
        }
    }
}