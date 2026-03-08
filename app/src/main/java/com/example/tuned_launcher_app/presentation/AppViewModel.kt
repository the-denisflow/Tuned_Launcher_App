package com.example.tuned_launcher_app.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tuned_launcher_app.data.repository.AppRepository
import com.example.tuned_launcher_app.domain.model.AppInfo
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class AppViewModel(repository: AppRepository) : ViewModel() {

    val apps: StateFlow<List<AppInfo>> = repository.getApps()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MS),
            initialValue = emptyList()
        )

    companion object {
        private const val STOP_TIMEOUT_MS = 5_000L
    }
}