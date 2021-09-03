package com.technzone.miniborsa.utils.validation

import android.content.Context
import com.technzone.miniborsa.R

class Validator() {

    companion object {

        private val FIRST_NAME_MIN_LENGTH = 3
        private val LAST_NAME_MIN_LENGTH = 3
        private val OTP_LENGTH = 5
        private val MINIMUM_COUNT = 1


        //Email
        const val EMAIL_REGEX = "[A-Z0-9a-z._%-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}"

        //Phone Number
        const val JORDANIAN_PHONE_NUMBER_WITHOUT_COUNTRY_CODE_REGEX = "^(7|07)(7|8|9)([0-9]{7})\$"
        const val PHONE_MIN_LENGTH = 9

        //Text
        const val VALID_TEXT_REGEX = "(?<! )[-a-zA-Z0-9\\u0600-\\u06FF ]*"
        const val VALID_TEXT_AND_NUMBERS_REGEX =
            "(?<! )[0-9-a-zA-Z\\u0600-\\u06FF\\u0660-\\u0669 ]*"
        const val VALID_DATA_REGEX = "(?<! )[-a-zA-Z0-9 ]*"

        // Password
        const val PASSWORD_REGEX =
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$"

        //Date
        const val DATE_REGEX =
            "([12]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]))"

        //Time
        const val TIME_REGEX = "^(2[0-3]|[01]?[0-9]):([0-5]?[0-9])$"
//            old
//            "([01]?[0-9]|2[0-3]):[0-5][0-9]"
        /**
         * todo fix TIME REGEX TO VALID 12:00 OR 1:00
         */
    }

    data class ValidatedData(
        val isValid: Boolean,
        val errorTitle: String,
        val errorMessage: String
    )

    lateinit var context: Context

    private var textToValidate: String = ""


    fun validate(
        validatorInputTypesEnums: ValidatorInputTypesEnums,
        input: String,
        context: Context
    ): ValidatedData {
        this.textToValidate = input
        this.context = context
        return when (validatorInputTypesEnums) {
            ValidatorInputTypesEnums.FULL_NAME -> {
                validateFullName()
            }
            ValidatorInputTypesEnums.FIRST_NAME -> {
                validateFirstName()
            }
            ValidatorInputTypesEnums.LAST_NAME -> {
                validateLastName()
            }
            ValidatorInputTypesEnums.EMAIL -> {
                validateEmail()
            }
            ValidatorInputTypesEnums.PHONE_NUMBER -> {
                validatePhoneNumber()
            }
            ValidatorInputTypesEnums.PASSWORD -> {
                validatePassword()
            }
            ValidatorInputTypesEnums.TEXT -> {
                validateText()
            }
            ValidatorInputTypesEnums.DATE -> {
                validateDate()
            }
            ValidatorInputTypesEnums.TIME -> {
                validateTime()
            }
            ValidatorInputTypesEnums.OTP -> {
                validateOTP()
            }
            ValidatorInputTypesEnums.COUNT -> {
                validateCount()
            }
            ValidatorInputTypesEnums.SEARCH_TEXT -> {
                validateSearchText()
            }
            else -> ValidatedData(true, "", "")
        }
    }

    fun validate(
        validatorInputTypesEnums: ValidatorInputTypesEnums, password:
        String, confirmPassword: String, context: Context
    ): ValidatedData {
        this.textToValidate = password
        this.context = context
        return when (validatorInputTypesEnums) {
            ValidatorInputTypesEnums.CONFIRM_PASSWORD -> {
                validatePasswordMatchConfirmPassword(confirmPassword)
            }
            else -> ValidatedData(true, "", "")
        }
    }

    private fun validateFullName(): ValidatedData {
        return if (textToValidate.isNullOrEmpty()) {
            return ValidatedData(
                isValid = false,
                errorTitle = context.resources.getString(R.string.full_name),
                errorMessage = context.resources.getString(R.string.must_not_be_empty)
            )
        } else if (textToValidate?.length ?: 0 < FIRST_NAME_MIN_LENGTH) {
            ValidatedData(
                isValid = false,
                errorTitle = context.resources.getString(R.string.full_name),
                errorMessage = context.resources.getString(R.string.must_be_at_least) + " " +
                        FIRST_NAME_MIN_LENGTH + " " + context.resources.getString(R.string.characters)
            )
        } else ValidatedData(true, "", "")
    }

    private fun validateFirstName(): ValidatedData {
        return if (textToValidate.isNullOrEmpty()) {
            return ValidatedData(
                isValid = false,
                errorTitle = context.resources.getString(R.string.first_name),
                errorMessage = context.resources.getString(R.string.must_not_be_empty)
            )
        } else if (textToValidate?.length ?: 0 < FIRST_NAME_MIN_LENGTH) {
            ValidatedData(
                isValid = false,
                errorTitle = context.resources.getString(R.string.first_name),
                errorMessage = context.resources.getString(R.string.must_be_at_least) + " " +
                        FIRST_NAME_MIN_LENGTH + " " + context.resources.getString(R.string.characters)
            )
        } else ValidatedData(true, "", "")
    }

    private fun validateLastName(): ValidatedData {
        return if (textToValidate.isNullOrEmpty()) {
            return ValidatedData(
                isValid = false,
                errorTitle = context.resources.getString(R.string.last_name),
                errorMessage = context.resources.getString(R.string.must_not_be_empty)
            )
        } else if (textToValidate?.length ?: 0 < LAST_NAME_MIN_LENGTH) {
            ValidatedData(
                isValid = false,
                errorTitle = context.resources.getString(R.string.last_name),
                errorMessage = context.resources.getString(R.string.must_be_at_least) + " " +
                        LAST_NAME_MIN_LENGTH + " " + context.resources.getString(R.string.characters)
            )
        } else ValidatedData(true, "", "")
    }

    private fun validateEmail(): ValidatedData {
        return if (textToValidate.isNullOrEmpty()) {
            return ValidatedData(
                isValid = false,
                errorTitle = context.resources.getString(R.string.email),
                errorMessage = context.resources.getString(R.string.must_not_be_empty)
            )
        } else if (!textToValidate.matches(Regex(EMAIL_REGEX))) {
            ValidatedData(
                isValid = false,
                errorTitle = context.resources.getString(R.string.email),
                errorMessage = context.resources.getString(R.string.email_not_valid_err)
            )
        } else ValidatedData(true, "", "")
    }

    private fun validatePhoneNumber(): ValidatedData {
        return if (textToValidate.isNullOrEmpty()) {
            return ValidatedData(
                isValid = false,
                errorTitle = context.resources.getString(R.string.phone_number),
                errorMessage = context.resources.getString(R.string.must_not_be_empty)
            )
        } else if (textToValidate.length < PHONE_MIN_LENGTH) {
            ValidatedData(
                isValid = false,
                errorTitle = context.resources.getString(R.string.phone_number),
                errorMessage = context.resources.getString(R.string.must_be_at_least) + " " +
                        PHONE_MIN_LENGTH + " " + context.resources.getString(R.string.numbers)
            )
        } else if (!textToValidate.matches(Regex(JORDANIAN_PHONE_NUMBER_WITHOUT_COUNTRY_CODE_REGEX))) {
            ValidatedData(
                isValid = false,
                errorTitle = context.resources.getString(R.string.phone_number),
                errorMessage = context.resources.getString(R.string.phone_not_valid_err)
            )
        } else ValidatedData(true, "", "")
    }

    private fun validatePassword(): ValidatedData {
        return if (textToValidate.isNullOrEmpty()) {
            return ValidatedData(
                isValid = false,
                errorTitle = context.resources.getString(R.string.password),
                errorMessage = context.resources.getString(R.string.must_not_be_empty)
            )
        } else if (!textToValidate.matches(Regex(PASSWORD_REGEX))) {
            ValidatedData(
                isValid = false,
                errorTitle = context.resources.getString(R.string.password),
                errorMessage = context.resources.getString(R.string.password_not_valid_err)
            )
        } else ValidatedData(true, "", "")
    }

    private fun validatePasswordMatchConfirmPassword(confirmPassword: String): ValidatedData {
        return if (textToValidate.isNullOrEmpty()) {
            return ValidatedData(
                isValid = false,
                errorTitle = context.resources.getString(R.string.confirm_password),
                errorMessage = context.resources.getString(R.string.must_not_be_empty)
            )
        } else if (!textToValidate.equals(confirmPassword)) {
            ValidatedData(
                isValid = false,
                errorTitle = context.resources.getString(R.string.confirm_password),
                errorMessage = context.resources.getString(R.string.password_not_match)
            )
        } else ValidatedData(true, "", "")
    }

    private fun validateText(): ValidatedData {
        return if (textToValidate.isNullOrEmpty()) {
            return ValidatedData(
                isValid = false,
                errorTitle = context.resources.getString(R.string.text),
                errorMessage = context.resources.getString(R.string.must_not_be_empty)
            )
        } else if (!textToValidate.matches(Regex(VALID_TEXT_REGEX))) {
            ValidatedData(
                isValid = false,
                errorTitle = context.resources.getString(R.string.text),
                errorMessage = context.resources.getString(R.string.text_not_valid_err)
            )
        } else ValidatedData(true, "", "")
    }

    private fun validateDate(): ValidatedData {
        return if (textToValidate.isNullOrEmpty()) {
            return ValidatedData(
                isValid = false,
                errorTitle = context.resources.getString(R.string.hint_date),
                errorMessage = context.resources.getString(R.string.must_not_be_empty)
            )
        } else if (!textToValidate.matches(Regex(DATE_REGEX))) {
            ValidatedData(
                isValid = false,
                errorTitle = context.resources.getString(R.string.hint_date),
                errorMessage = context.resources.getString(R.string.date_not_valid_err)
            )
        } else ValidatedData(true, "", "")
    }

    private fun validateTime(): ValidatedData {
        return if (textToValidate.isNullOrEmpty()) {
            return ValidatedData(
                isValid = false,
                errorTitle = context.resources.getString(R.string.time),
                errorMessage = context.resources.getString(R.string.must_not_be_empty)
            )
        } else if (!textToValidate.matches(Regex(TIME_REGEX))) {
            ValidatedData(
                isValid = false,
                errorTitle = context.resources.getString(R.string.time),
                errorMessage = context.resources.getString(R.string.time_not_valid_err)
            )
        } else ValidatedData(true, "", "")
    }

    private fun validateOTP(): ValidatedData {
        return if (textToValidate.isNullOrEmpty()) {
            return ValidatedData(
                isValid = false,
                errorTitle = context.resources.getString(R.string.otp),
                errorMessage = context.resources.getString(R.string.must_not_be_empty)
            )
        } else if (textToValidate.length ?: 0 < OTP_LENGTH) {
            ValidatedData(
                isValid = false,
                errorTitle = context.resources.getString(R.string.otp),
                errorMessage = context.resources.getString(R.string.must_be_at_least) + " " +
                        OTP_LENGTH + " " + context.resources.getString(R.string.digits)
            )
        } else ValidatedData(true, "", "")
    }

    private fun validateCount(): ValidatedData {
        try {
            val value = textToValidate.toInt()

            return if (textToValidate.isNullOrEmpty()) {
                return ValidatedData(
                    isValid = false,
                    errorTitle = context.resources.getString(R.string.number),
                    errorMessage = context.resources.getString(R.string.must_not_be_empty)
                )
            } else if (value < MINIMUM_COUNT) {
                ValidatedData(
                    isValid = false,
                    errorTitle = context.resources.getString(R.string.number),
                    errorMessage = context.resources.getString(R.string.must_be_at_least)
                )
            } else ValidatedData(true, "", "")

        } catch (ex: NumberFormatException) {
            println("The given string is non-numeric")
            ValidatedData(true, "", "")
        }
        return ValidatedData(true, "", "")
    }

    private fun validateSearchText(): ValidatedData {
        return if (textToValidate.isNullOrEmpty()) {
            return ValidatedData(
                isValid = false,
                errorTitle = context.resources.getString(R.string.text),
                errorMessage = context.resources.getString(R.string.must_not_be_empty)
            )
        } else if (!textToValidate.matches(Regex(VALID_TEXT_REGEX))) {
            ValidatedData(
                isValid = false,
                errorTitle = context.resources.getString(R.string.text),
                errorMessage = context.resources.getString(R.string.text_not_valid_err)
            )
        } else ValidatedData(true, "", "")
    }

}