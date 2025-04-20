package com.shabelnikd.rickandmorty.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named

abstract class BaseViewModel : ViewModel(), KoinComponent {

    private val ioDispatcher = Dispatchers.IO

    sealed class UiState<out T> {
        data object NotLoaded : UiState<Nothing>()
        data object Loading : UiState<Nothing>()
        data class Success<T>(val data: T) : UiState<T>()
        data class Error(val message: String) : UiState<Nothing>()
    }

    protected fun <T> collectFlow(
        request: suspend () -> Flow<Result<T>>,
        stateFlow: MutableStateFlow<UiState<T>>,
        onSuccess: suspend (T) -> Unit = {},
        onError: suspend (Throwable) -> Unit = {}
    ) {
        viewModelScope.launch(ioDispatcher) {
            stateFlow.value = UiState.Loading

            request().collect { result ->
                result.onSuccess { data ->
                    stateFlow.value = UiState.Success(data)
                    onSuccess(data)

                }.onFailure { error ->
                    stateFlow.value = UiState.Error(error.message ?: "An unexpected error occurred")
                    onError(error)
                }
            }
        }
    }
}