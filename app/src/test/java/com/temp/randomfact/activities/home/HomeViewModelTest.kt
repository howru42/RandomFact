package com.temp.randomfact.activities.home

import com.temp.randomfact.data.model.FactInfo
import com.temp.randomfact.data.repository.FactsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class FactsViewModelTest {

    @Mock
    private lateinit var factsRepository: FactsRepository
    private val mainDispatcher = Dispatchers.Unconfined

    private lateinit var viewModel: HomeViewModel

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(mainDispatcher)
        viewModel = HomeViewModel(factsRepository)
    }

    @After
    fun cleanup() {
        Dispatchers.resetMain()
    }

    @Test
    fun `get Random Facts`() = runTest {
        val selectedLanguage = "en"
        val isToday = false

        viewModel.getFacts()

        verify(factsRepository).getFactsFromDB(selectedLanguage, isToday)
    }

    @Test
    fun `get Today fact`() = runTest {
        val selectedLanguage = "en"
        val isToday = true

        viewModel.getFacts(isToday)

        verify(factsRepository).getFactsFromDB(selectedLanguage, isToday)
    }

    @Test
    fun `update read status for Fact`() = runTest {
        val factInfo = FactInfo(
            "1", "Sample", "google.com", "https://google.com", "en", "https://google.com"
        )
        viewModel.loadAnotherFact(factInfo)
        verify(factsRepository).updateReadStatus(factInfo)
    }

    @Test
    fun `Fetch random facts from API based on language`() = runTest {
        val selectedLanguage = "en"
        viewModel.fetchRandomFacts()
        verify(factsRepository).getRandomFactsFromAPI(selectedLanguage)
    }
}