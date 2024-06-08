package com.serkancay.compose.statemanagement.otp.event

sealed interface OtpEvent {
    data class OnInputChange(val input: String): OtpEvent
}