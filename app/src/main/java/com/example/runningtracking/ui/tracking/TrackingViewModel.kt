package com.example.runningtracking.ui.tracking

import androidx.lifecycle.viewModelScope
import com.example.runningtracking.base.BaseViewModel
import com.example.runningtracking.base.UiEffect
import com.example.runningtracking.base.UiEvent
import com.example.runningtracking.base.UiState
import com.example.runningtracking.model.Run
import com.example.runningtracking.rx.SchedulersProvider
import com.example.runningtracking.ui.main.MainRepository
import com.example.runningtracking.utils.SharedPreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class TrackingViewModel @Inject constructor(
    private val repo: MainRepository,
    schedulers: SchedulersProvider, sharedPreferencesManager: SharedPreferencesManager

): BaseViewModel<Event, State, Effect>(
    schedulers, sharedPreferencesManager,
) {

    override fun createInitialState(): State {
        return State()
    }

    override fun handleEvent(it: Event) {
        when(it){
            is Event.FinishRun -> {
                viewModelScope.launch(Dispatchers.IO){
                    val response = repo.uploadRunTracking(it.run)
                    if (response.isSuccessful){
                        val responseBody = checkNotNull(response.body()){"Response body is null"}
                        Timber.d("$responseBody")
                    }

                }
            }
        }
    }


}
sealed class Event: UiEvent{
    data class FinishRun(val run: Run): Event()
}

class Effect: UiEffect

data class State(val loading: Boolean = false): UiState
