package com.example.runningtracking.ui.run

import androidx.lifecycle.viewModelScope
import com.example.runningtracking.base.BaseViewModel
import com.example.runningtracking.base.UiEffect
import com.example.runningtracking.base.UiEvent
import com.example.runningtracking.base.UiState
import com.example.runningtracking.rx.SchedulersProvider
import com.example.runningtracking.ui.main.MainRepository
import com.example.runningtracking.ui.run.model.RunResponse
import com.example.runningtracking.utils.SharedPreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.internal.http2.Http2
import retrofit2.http.HTTP
import javax.inject.Inject

@HiltViewModel
class RunViewModel @Inject constructor(
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
        when(it){
            is Event.OnResume -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val response = repo.fetchRunData()
                    if (response.isSuccessful){
                        val responseBody = checkNotNull(response.body()){"Response body is null"}
                        withContext(Dispatchers.Main){
                            setState { copy(runResponse=responseBody) }
                        }
                    }
                }
            }
        }
    }
}

sealed class Event : UiEvent {
    data object OnResume : Event()
}

sealed class Effect : UiEffect
data class State(
    val isLoading: Boolean = false,
    var runResponse: List<RunResponse>? = null
) : UiState