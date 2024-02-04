package com.example.runningtracking.ui.authen.login

import androidx.lifecycle.viewModelScope
import com.example.runningtracking.base.BaseViewModel
import com.example.runningtracking.base.UiEffect
import com.example.runningtracking.base.UiEvent
import com.example.runningtracking.base.UiState
import com.example.runningtracking.rx.SchedulersProvider
import com.example.runningtracking.ui.authen.login.model.LoginRequest
import com.example.runningtracking.ui.main.MainRepository
import com.example.runningtracking.utils.SharedPreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repo: MainRepository,
    schedulers: SchedulersProvider,
    sharedPreferencesManager: SharedPreferencesManager
) : BaseViewModel<Event, State, Effect>(
    schedulers, sharedPreferencesManager
) {
    override fun createInitialState(): State {
        return State()
    }

    override fun handleEvent(it: Event) {
        when(it) {
            is Event.Login -> {
                viewModelScope.launch(Dispatchers.IO){
                    val response = repo.login(it.login)
                    if (response.isSuccessful){
                        val responseBody = checkNotNull(response.body()){"Response body is null"}
                        sharedPreferencesManager.token = responseBody.token.toString()
                        sharedPreferencesManager.refreshToken = responseBody.refreshToken.toString()
                        withContext(Dispatchers.Main){
                            setEffect { Effect.Success }
                        }
                    }
                    else {
                        withContext(Dispatchers.Main){
                            setEffect { Effect.Failure }
                        }
                    }
                }

            }
        }
    }
}

sealed class Event : UiEvent {
    data class Login(val login: LoginRequest): Event()
}

data class State(
    val isLoading: Boolean = false
) : UiState

sealed class Effect : UiEffect {
    data object Success : Effect()
    data object Failure: Effect()
}
