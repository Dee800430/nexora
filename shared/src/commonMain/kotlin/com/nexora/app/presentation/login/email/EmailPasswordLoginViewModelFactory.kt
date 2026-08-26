// EmailPasswordLoginViewModelFactory.kt
package com.nexora.app.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.nexora.app.data.repository.UserRepository
import kotlin.reflect.KClass

class EmailPasswordLoginViewModelFactory(
    private val userRepository: UserRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: KClass<T>,
                                        extras: CreationExtras): T {
        if (modelClass==EmailPasswordLoginViewModel::class) {
            return EmailPasswordLoginViewModel(userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}