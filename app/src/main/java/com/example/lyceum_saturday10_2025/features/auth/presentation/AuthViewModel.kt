package com.example.lyceum_saturday10_2025.features.auth.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.lyceum_saturday10_2025.common.UserPrefsManager
import com.example.lyceum_saturday10_2025.features.auth.data.AuthRepository
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = AuthRepository(application.applicationContext)
    private val prefs = UserPrefsManager(application.applicationContext)

    fun register(username: String, password: String, onSuccess: () -> Unit, onError: (Throwable) -> Unit) {
        viewModelScope.launch {
            try {
                val tokens = repository.register(username, password)
                prefs.username = username
                prefs.accessToken = tokens.access_token
                prefs.refreshToken = tokens.refresh_token
                onSuccess()
            } catch (e: Throwable) {
                onError(e)
            }
        }
    }

    fun login(username: String, password: String, onSuccess: () -> Unit, onError: (Throwable) -> Unit) {
        viewModelScope.launch {
            try {
                val tokens = repository.login(username, password)
                prefs.username = username
                prefs.accessToken = tokens.access_token
                prefs.refreshToken = tokens.refresh_token
                onSuccess()
            } catch (e: Throwable) {
                onError(e)
            }
        }
    }
}