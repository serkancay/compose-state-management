package com.serkancay.compose.statemanagement.otp.model

sealed interface OtpStatusUIModel {
    data class Verified(val message: String) : OtpStatusUIModel
    data class Unverified(val message: String) : OtpStatusUIModel
    data object Empty : OtpStatusUIModel
}