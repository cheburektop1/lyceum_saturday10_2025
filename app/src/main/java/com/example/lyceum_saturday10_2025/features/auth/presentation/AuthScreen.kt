package com.example.lyceum_saturday10_2025.features.auth.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import android.widget.Toast
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
@Destination
fun AuthScreen(
    navigator: DestinationsNavigator
) {
    val viewModel = viewModel<AuthViewModel>()
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Имя пользователя") },
            modifier = Modifier.padding(vertical = 8.dp)
        )
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Пароль") },
            modifier = Modifier.padding(vertical = 8.dp)
        )

        if (isLoading) {
            Text("Загрузка...")
        }

        Button(
            onClick = {
                if (isLoading) return@Button
                if (username.isBlank() || password.isBlank()) {
                    Toast.makeText(context, "Введите имя пользователя и пароль", Toast.LENGTH_SHORT).show()
                } else {
                    isLoading = true
                    viewModel.register(username, password, onSuccess = {
                        isLoading = false
                        Toast.makeText(context, "Регистрация успешна", Toast.LENGTH_SHORT).show()
                        navigator.navigateUp()
                    }, onError = { e ->
                        isLoading = false
                        Toast.makeText(context, "Ошибка: ${e.message}", Toast.LENGTH_LONG).show()
                    })
                }
            },
            enabled = !isLoading
        ) {
            Text("Зарегистрироваться")
        }

        Button(
            onClick = {
                if (isLoading) return@Button
                if (username.isBlank() || password.isBlank()) {
                    Toast.makeText(context, "Введите имя пользователя и пароль", Toast.LENGTH_SHORT).show()
                } else {
                    isLoading = true
                    viewModel.login(username, password, onSuccess = {
                        isLoading = false
                        Toast.makeText(context, "Вход успешен", Toast.LENGTH_SHORT).show()
                        navigator.navigateUp()
                    }, onError = { e ->
                        isLoading = false
                        Toast.makeText(context, "Ошибка: ${e.message}", Toast.LENGTH_LONG).show()
                    })
                }
            },
            enabled = !isLoading
        ) {
            Text("Войти")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AuthPreview() {
    // превью без навигатора
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Auth preview")
    }
}