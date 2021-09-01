package com.technzone.miniborsa.data.api.response

enum class ResponseSubErrorsCodeEnum(val value: Int) {
    GENERAL_FAILED(-1),
    Success(200),
    EmailNotVerified(9),
    InvalidModel(1),
    Unauthorized(401),
    Forbidden(403),
    NotFound(404);

    companion object {
        fun getResponseSubErrorsCodeEnumByValue(value: Int): ResponseSubErrorsCodeEnum {
            return when (value) {
                0 -> Success
                1 -> InvalidModel
                9 -> EmailNotVerified
                401 -> Unauthorized
                403 -> Forbidden
                404 -> NotFound
                else -> GENERAL_FAILED
            }
        }
    }
}