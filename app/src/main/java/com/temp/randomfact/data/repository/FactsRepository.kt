package com.temp.randomfact.data.repository

import android.util.Log
import com.temp.randomfact.data.model.FactInfo
import com.temp.randomfact.data.repository.local.FactsDao
import com.temp.randomfact.data.repository.remote.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import java.text.SimpleDateFormat
import java.util.Date

class FactsRepository(
    private val apiService: ApiService, private val factsDao: FactsDao
) {
    private val _factsStateFlow: MutableStateFlow<State> = MutableStateFlow(State.EMPTY)
    val factsStateFlow: Flow<State> = _factsStateFlow

    suspend fun getFactsFromDB(language: String, isToday: Boolean = false) {
        _factsStateFlow.value = State.Loading
        try {
            val date = SimpleDateFormat.getDateInstance().format(Date())
            if (isToday) {
                val factInfo: FactInfo? = factsDao.getTodayFact(language, date).first()
                if (factInfo != null) {
                    _factsStateFlow.value = State.Success(factInfo)
                } else {
                    getTodayFactFromAPI(language)
                }
            } else {
                val factInfo = factsDao.getFact(language, date).first()
                if (factInfo != null) {
                    _factsStateFlow.value = State.Success(factInfo)
                }
                getRandomFactsFromAPI(language)
            }
        } catch (e: Exception) {
            val errorMessage = "Error: ${e.message}"
            _factsStateFlow.value = State.Error(errorMessage)
        }
    }

    private suspend fun getTodayFactFromAPI(language: String) {
        _factsStateFlow.value = State.Loading
        try {
            val fact: FactInfo = apiService.getTodayFact(language)
//            Log.e("TAG", "getTodayFactFromAPI" + fact)
            fact.publishDate = Date()
            factsDao.insertFact(fact)
            _factsStateFlow.value = State.Success(fact)
        } catch (e: Exception) {
            val errorMessage = "Error: ${e.message}"
            _factsStateFlow.value = State.Error(errorMessage)
        }
    }

    suspend fun updateReadStatus(factInfo: FactInfo) {
        factInfo.isRead = true
        factsDao.updateReadStatus(factInfo)
    }

    suspend fun getRandomFactsFromAPI(language: String) {
        val fact: FactInfo = apiService.getRandomFact(language)
//        Log.e("TAG", "getRandomFactsFromAPI:" + fact)
        factsDao.insertFact(fact)
    }
}

sealed class State {
    object Loading : State()
    data class Success(val factInfo: FactInfo) : State()
    data class Error(val message: String) : State()
    object EMPTY : State()
}
