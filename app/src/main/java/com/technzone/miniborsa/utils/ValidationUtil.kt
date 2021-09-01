package com.technzone.miniborsa.utils

import java.util.regex.Pattern

object ValidationUtil{


    private const val NUMBERS_RE =
        "^[+]?[0-9]*$" // regular expression to know if all char in string is numbers , and can start with (+)
    private const val EMAIL_REGEX = "[A-Z0-9a-z._%-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}"

    private fun notNullOrEmpty(text: String?): Boolean {
        return try {
            text != null && text.isNotEmpty()
        } catch (e: Exception) {
            false
        }
    }

    fun isEmailValid(email: String?): Boolean {
        return try {
            notNullOrEmpty(email) && Pattern.compile(
                EMAIL_REGEX
            ).matcher(email!!)
                .matches()
        } catch (e: Exception) {
            false
        }
    }
}