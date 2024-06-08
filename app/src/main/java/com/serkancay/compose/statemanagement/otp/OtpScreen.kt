package com.serkancay.compose.statemanagement.otp

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.serkancay.compose.statemanagement.otp.event.OtpEvent
import com.serkancay.compose.statemanagement.otp.model.OtpStatusUIModel
import com.serkancay.compose.statemanagement.otp.model.OtpUIModel
import com.serkancay.compose.statemanagement.otp.state.OtpUIState

@Composable
fun OtpRoute(
    viewModel: OtpViewModel = viewModel()
) {

    val uiState = viewModel.uiState.collectAsState()

    OtpScreen(
        uiState = uiState.value,
        onEvent = { event ->
            viewModel.onEvent(event)
        }
    )
}

@Composable
fun OtpScreen(
    uiState: OtpUIState,
    onEvent: (OtpEvent) -> Unit
) {
    when (uiState) {
        is OtpUIState.Empty -> {}

        is OtpUIState.Loading -> {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(48.dp)
                        .align(Alignment.Center)
                )
            }
        }

        is OtpUIState.Error -> {
            Box(modifier = Modifier.fillMaxSize()) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = uiState.message,
                    color = Color.Red
                )
            }
        }

        is OtpUIState.Success -> {

            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = uiState.data.infoMessage)
                Spacer(modifier = Modifier.height(16.dp))
                BasicTextField(
                    value = uiState.data.input,
                    onValueChange = {
                        onEvent.invoke(OtpEvent.OnInputChange(it))
                    },
                    decorationBox = {
                        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            repeat(uiState.data.inputSize) { index ->
                                val char = when {
                                    index >= uiState.data.input.length -> ""
                                    else -> uiState.data.input[index].toString()
                                }
                                Text(
                                    modifier = Modifier
                                        .size(48.dp)
                                        .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                                        .wrapContentSize(),
                                    text = char,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                )
                when (val otpStatus = uiState.data.status) {
                    is OtpStatusUIModel.Verified -> {
                        Text(
                            modifier = Modifier.padding(vertical = 8.dp),
                            text = otpStatus.message,
                            color = Color.Green
                        )
                    }

                    is OtpStatusUIModel.Unverified -> {
                        Text(
                            modifier = Modifier.padding(vertical = 8.dp),
                            text = otpStatus.message,
                            color = Color.Red
                        )
                    }

                    is OtpStatusUIModel.Empty -> {}
                }
            }


        }
    }
}

@Composable
@Preview(showBackground = true)
fun OtpScreenPreview() {
    OtpScreen(
        uiState = OtpUIState.Success(
            data = OtpUIModel(
                infoMessage = "Otp Screen",
                input = "12"
            )
        ),
        onEvent = {})
}