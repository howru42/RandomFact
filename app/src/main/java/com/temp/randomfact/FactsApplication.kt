package com.temp.randomfact

import android.app.Application
import com.temp.randomfact.data.repository.FactsRepository
import com.temp.randomfact.data.repository.local.FactsDao
import com.temp.randomfact.data.repository.local.FactsDatabase
import com.temp.randomfact.data.repository.remote.ApiClient

class FactsApplication : Application() {
    private val apiService by lazy { ApiClient.apiService }
    private val factsDao: FactsDao by lazy {
        FactsDatabase.getDatabase(applicationContext).factsDao()
    }
    val factsRepository by lazy { FactsRepository(apiService, factsDao) }
}