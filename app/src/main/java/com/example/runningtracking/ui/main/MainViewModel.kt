package com.example.runningtracking.ui.main

import com.example.runningtracking.base.BaseViewModel
import com.example.runningtracking.base.UiEffect
import com.example.runningtracking.base.UiEvent
import com.example.runningtracking.base.UiState
import com.example.runningtracking.rx.SchedulersProvider
import com.example.runningtracking.utils.SharedPreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    repo: MainRepository,
    schedulers: SchedulersProvider,
    sharedPreferencesManager: SharedPreferencesManager
) : BaseViewModel<Event, State, Effect>(
    schedulers, sharedPreferencesManager
) {
    override fun createInitialState(): State {
        return State()
    }

    override fun handleEvent(it: Event) {
    }

}

sealed class Event : UiEvent

data class State(
    val isLoading: Boolean = false
) : UiState

sealed class Effect : UiEffect