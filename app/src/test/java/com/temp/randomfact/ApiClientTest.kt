package com.temp.randomfact

import com.temp.randomfact.data.model.FactInfo
import com.temp.randomfact.data.repository.remote.ApiService
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import java.io.IOException

class ApiClientTest {
    @Test
    fun `test API service returns successful response`() = runBlocking {
        // Mock the API service
        val apiService = mock(ApiService::class.java)
        val response = FactInfo(
            "1",
            "Hello",
            "google.com",
            "https://www.google.com",
            "en",
            "https://www.google.com",
            null
        )

        `when`(apiService.getTodayFact("en")).thenReturn(response)

        // Make the API call
        val result = apiService.getTodayFact("en")

        // Assert the response is successful
        assertTrue(result.text == response.text)
    }


    @Test
    fun `test API service parses response correctly`() = runBlocking {
        // Mock the API service
        val apiService = mock(ApiService::class.java)
        val response = FactInfo(
            "1",
            "Hello",
            "google.com",
            "https://www.google.com",
            "en",
            "https://www.google.com",
            null
        )
        `when`(apiService.getRandomFact("en")).thenReturn(response)

        // Make the API call
        val result = apiService.getRandomFact("en")

        // Assert the response is parsed correctly
        assertNotNull(result)
    }
}