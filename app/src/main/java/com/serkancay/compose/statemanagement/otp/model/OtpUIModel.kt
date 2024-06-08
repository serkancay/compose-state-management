package com.serkancay.compose.statemanagement.otp.model

const val DEFAULT_OTP_SIZE = 6

data class OtpUIModel(
    val infoMessage: String = "",
    val input: String = "",
    val inputSize: Int = DEFAULT_OTP_SIZE,
    val status: OtpStatusUIModel = OtpStatusUIModel.Empty
)