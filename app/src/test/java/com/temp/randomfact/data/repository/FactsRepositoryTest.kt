package com.temp.randomfact.data.repository

import com.nhaarman.mockitokotlin2.verify
import com.temp.randomfact.data.model.FactInfo
import com.temp.randomfact.data.repository.local.FactsDao
import com.temp.randomfact.data.repository.remote.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import java.text.SimpleDateFormat
import java.util.Date


@ExperimentalCoroutinesApi
class FactsRepositoryTest {
    lateinit var factsRepository: FactsRepository

    @Mock
    private lateinit var factsDao: FactsDao

    @Mock
    private lateinit var apiService: ApiService

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        factsRepository = FactsRepository(apiService, factsDao)
    }

    @After
    fun cleanup() {
        Dispatchers.resetMain()
    }


    @Test
    fun `getFactsFromDB when isToday=true and factInfo is not null`() = runTest {
        val language = "en"
        val isToday = true
        val date = SimpleDateFormat.getDateInstance().format(Date())

        val mockFactInfo = FactInfo(
            "1", "Sample", "google.com", "https://google.com", "en", "https://google.com"
        )

        `when`(factsDao.getTodayFact(language, date)).thenReturn(flowOf(mockFactInfo))

        factsRepository.getFactsFromDB(language, isToday)

        val result = factsRepository.factsStateFlow.first()

        assertEquals(State.Success(mockFactInfo), result)
    }

    @Test
    fun `getFactsFromDB when isToday=true and factInfo is null`() = runTest {
        val language = "en"
        val isToday = true
        val date = SimpleDateFormat.getDateInstance().format(Date())

        `when`(factsDao.getTodayFact(language, date)).thenReturn(flowOf(null))
        val mockFactInfo = FactInfo(
            "1", "Sample", "google.com", "https://google.com", "en", "https://google.com"
        )
        `when`(apiService.getTodayFact(language)).thenReturn(mockFactInfo)
        factsRepository.getFactsFromDB(language, isToday)

        val result = factsRepository.factsStateFlow.first()

        assertEquals(State.Success(mockFactInfo), result) // Expect initial loading state
    }

    @Test
    fun `getFactsFromDB when isToday=false and factInfo is not null`() = runTest {
        val language = "en"
        val isToday = false
        val date = SimpleDateFormat.getDateInstance().format(Date())

        val mockFactInfo = FactInfo(
            "1", "Sample", "google.com", "https://google.com", "en", "https://google.com"
        )

        `when`(factsDao.getFact(language, date)).thenReturn(flowOf(mockFactInfo))

        factsRepository.getFactsFromDB(language, isToday)

        val result = factsRepository.factsStateFlow.first()

        assertEquals(State.Success(mockFactInfo), result)
    }

    @Test
    fun `getFactsFromDB when isToday=false and factInfo is null`() = runTest {
        val language = "en"
        val isToday = false
        val date = SimpleDateFormat.getDateInstance().format(Date())

        `when`(factsDao.getFact(language, date)).thenReturn(flowOf(null))

        factsRepository.getFactsFromDB(language, isToday)

        val result = factsRepository.factsStateFlow.first()

        assertEquals(State.Loading, result) // Expect initial loading state
    }

    @Test
    fun `Update Read status of fact`() = runTest {
         val mockFactInfo = FactInfo(
            "1", "Sample", "google.com", "https://google.com", "en", "https://google.com"
        )
        factsRepository.updateReadStatus(mockFactInfo)
        verify(factsDao).updateReadStatus(mockFactInfo)
    }

}
