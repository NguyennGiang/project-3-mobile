package com.example.runningtracking.ui.statistics

import androidx.lifecycle.viewModelScope
import com.example.runningtracking.base.BaseViewModel
import com.example.runningtracking.base.UiEffect
import com.example.runningtracking.base.UiEvent
import com.example.runningtracking.base.UiState
import com.example.runningtracking.rx.SchedulersProvider
import com.example.runningtracking.ui.main.MainRepository
import com.example.runningtracking.ui.run.model.RunResponse
import com.example.runningtracking.ui.statistics.model.StatisticResponse
import com.example.runningtracking.utils.SharedPreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class StatisticViewModel @Inject constructor(
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
        when (it) {
            is Event.OnResume -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val response = repo.fetchStatisticData()
                    if (response.isSuccessful) {
                        val responseBody = checkNotNull(response.body()) {}
                        withContext(Dispatchers.Main) {
                            setState { copy(statistic = responseBody) }
                        }
                    } else {
                        setEffect { Effect.Failure }
                    }

                    val runs = repo.fetchRunData()
                    if (runs.isSuccessful) {
                        val runsBody = checkNotNull(runs.body()) {}
                        withContext(Dispatchers.Main) {
                            setState { copy(runs = runsBody) }
                        }
                    } else {
                        setEffect { Effect.Failure }
                    }
                }
            }
        }
    }

}

sealed class Event : UiEvent {
    data object OnResume : Event()
}

data class State(
    val isLoading: Boolean = false,
    val statistic: StatisticResponse? = null,
    val runs: List<RunResponse>? = null
) : UiState

sealed class Effect : UiEffect {
    data object Failure : Effect()
}