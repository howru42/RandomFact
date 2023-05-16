package com.temp.randomfact.activities.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.temp.randomfact.FactsApplication
import com.temp.randomfact.data.model.FactInfo
import com.temp.randomfact.data.repository.FactsRepository
import com.temp.randomfact.data.repository.State
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(private val factsRepository: FactsRepository) : ViewModel() {
    private val _factsStateFlow: MutableStateFlow<State> = MutableStateFlow(State.EMPTY)
    val factsStateFlow: StateFlow<State> = _factsStateFlow
    var selectedLanguage: String = LANGUAGE.ENGLISH.locale

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>, extras: CreationExtras
            ): T {
                val application = checkNotNull(extras[APPLICATION_KEY])
                return HomeViewModel((application as FactsApplication).factsRepository) as T
            }
        }
    }

    fun init() {
        viewModelScope.launch {
            factsRepository.factsStateFlow.collect { state ->
                _factsStateFlow.value = state
            }
        }
    }

    fun getFacts(isToday: Boolean = false) {
        viewModelScope.launch {
            factsRepository.getFactsFromDB(selectedLanguage, isToday)
        }
    }

    fun loadAnotherFact(factInfo: FactInfo?) {
        factInfo?.let {
            viewModelScope.launch {
                factsRepository.updateReadStatus(factInfo)
                factsRepository.getFactsFromDB(selectedLanguage)
            }
        }
    }

    fun fetchRandomFacts() {
        viewModelScope.launch {
            factsRepository.getRandomFactsFromAPI(selectedLanguage)
        }
    }
}

