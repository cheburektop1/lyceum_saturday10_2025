package com.example.lyceum_saturday10_2025.features.todo.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lyceum_saturday10_2025.common.UserPrefsManager
import com.example.lyceum_saturday10_2025.features.destinations.AuthScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
@Destination(start = true)
fun TodoScreen(
    navigator: DestinationsNavigator
) {
    val context = LocalContext.current
    val prefs = UserPrefsManager(context)

    // если не авторизован — перенаправить на экран входа
    if (prefs.username == null || prefs.accessToken == null) {
        LaunchedEffect(Unit) {
            navigator.navigate(AuthScreenDestination())
        }
        return
    }

    val viewmodel = viewModel<TodoViewModel>()
    val state by viewmodel.state.collectAsState()

    TodoScreenContent(state, onAdd = { text ->
        viewmodel.addItem(text)
    }, onLogout = {
        prefs.clearUser()
        navigator.navigate(AuthScreenDestination())
    })
}