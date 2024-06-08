package com.serkancay.compose.statemanagement.otp.state

import com.serkancay.compose.statemanagement.otp.model.OtpUIModel

sealed interface OtpUIState {

    val data: OtpUIModel

    data class Empty(override val data: OtpUIModel = OtpUIModel()) : OtpUIState
    data class Loading(override val data: OtpUIModel) : OtpUIState
    data class Error(val message: String, override val data: OtpUIModel) : OtpUIState
    data class Success(override val data: OtpUIModel) : OtpUIState

}
