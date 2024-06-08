package com.serkancay.compose.statemanagement.otp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.serkancay.compose.statemanagement.otp.event.OtpEvent
import com.serkancay.compose.statemanagement.otp.model.OtpStatusUIModel
import com.serkancay.compose.statemanagement.otp.state.OtpUIState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class OtpViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<OtpUIState>(OtpUIState.Empty())
    val uiState get() = _uiState.asStateFlow()

    init {
        getOTPInformation()
    }

    private fun getOTPInformation() {
        viewModelScope.launch {
            _uiState.update { currentState ->
                OtpUIState.Loading(data = currentState.data)
            }
            delay(500)
            _uiState.update { currentState ->
                OtpUIState.Success(
                    data = currentState.data.copy(infoMessage = "Enter the OTP code sent via SMS")
                )
            }
        }
    }

    private fun verifyOTP(input: String) {
        viewModelScope.launch {
            _uiState.update { currentState ->
                OtpUIState.Loading(data = currentState.data)
            }
            delay(500)
            _uiState.update { currentState ->
                if (CORRECT_OTP_CODE == input) {
                    OtpUIState.Success(
                        data = currentState.data.copy(
                            input = "",
                            status = OtpStatusUIModel.Verified("Verified successfully")
                        )
                    )
                } else if (WRONG_OTP_CODE == input) {
                    OtpUIState.Success(
                        data = currentState.data.copy(
                            input = "",
                            status = OtpStatusUIModel.Unverified("Verification failed")
                        )
                    )
                } else {
                    OtpUIState.Error(
                        message = "An unexpected error occurred",
                        data = currentState.data.copy()
                    )
                }
            }
        }
    }

    fun onEvent(event: OtpEvent) {
        when (event) {
            is OtpEvent.OnInputChange -> {
                viewModelScope.launch {
                    _uiState.update { currentState ->
                        OtpUIState.Success(
                            data = currentState.data.copy(input = event.input)
                        )
                    }
                }
                if (event.input.length == uiState.value.data.inputSize) {
                    verifyOTP(event.input)
                }
            }
        }
    }

    companion object {
        const val CORRECT_OTP_CODE = "111111"
        const val WRONG_OTP_CODE = "000000"
    }

}