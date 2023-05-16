package com.temp.randomfact

import com.temp.randomfact.data.model.FactInfo
import com.temp.randomfact.data.repository.FactsRepository
import com.temp.randomfact.data.repository.local.FactsDao
import com.temp.randomfact.data.repository.remote.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import retrofit2.Response
import java.util.Date

class FactsRepositoryTest {

    @Mock
    private lateinit var apiService: ApiService

    @Mock
    private lateinit var factsDao: FactsDao

    private lateinit var factsRepository: FactsRepository

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        factsRepository = FactsRepository(apiService, factsDao)
    }

    @Test
    fun `Get today Fact from local DB`() = runBlocking {
        val factInfo = FactInfo(
            "123",
            "Mock Fact Text",
            "mock.google.com",
            "https://www.google.com",
            "en",
            "https://www.google.com", Date(),
        )

        `when`(factsDao.getTodayFact("en", Date().toString())).thenReturn(flowOf(factInfo))

        factsDao.getTodayFact("en", Date().toString()).collect { result ->
            assert(result == factInfo)
        }
    }

}