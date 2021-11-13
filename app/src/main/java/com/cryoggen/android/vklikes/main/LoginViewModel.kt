package com.cryoggen.android.vklikes.main

import android.app.Application
import android.content.Context
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(private val app: Application) : ViewModel() {

    private var _accessToken = MutableLiveData<String>()
    val accessToken: LiveData<String>
        get() = _accessToken

    private var prefs =
        app.getSharedPreferences("com.cryoggen.android.vklikes", Context.MODE_PRIVATE)

    private val TRIGGER_TOKEN = "TRIGGER_T"

    fun getToken() {
        viewModelScope.launch {
            loadToken()
        }
    }

    private suspend fun loadToken() {
        var token =""
        withContext(Dispatchers.IO) {
            token = prefs.getString(TRIGGER_TOKEN, "*").toString()
        }
        _accessToken.value = token
    }

    private suspend fun saveToken(token: String) {
        withContext(Dispatchers.IO) {
            prefs.edit().putString(TRIGGER_TOKEN, token).apply()
        }
    }

    fun refreshToken(token: String) {
        viewModelScope.launch {
            saveToken(token)
        }
        getToken()
    }

}

class LoginViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}