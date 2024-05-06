package com.plcoding.testingcourse.core.data

import assertk.assertThat
import assertk.assertions.isTrue
import com.plcoding.testingcourse.core.domain.AnalyticsLogger
import com.plcoding.testingcourse.core.domain.LogParam
import com.plcoding.testingcourse.core.domain.Product
import com.plcoding.testingcourse.core.domain.ProductRepository
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import retrofit2.HttpException

class ProductRepositoryImplTest {
    private lateinit var repository: ProductRepositoryImpl
    private lateinit var productApi: ProductApi
    private lateinit var analyticsLogger: AnalyticsLogger

    @BeforeEach
    fun setUp() {
        productApi = mockk()
        analyticsLogger = mockk(relaxed = true)
        repository = ProductRepositoryImpl(productApi, analyticsLogger)
    }

    @Test
    fun `Response error, log the error`() = runBlocking {
        //coEvery { analyticsLogger.logEvent(any(), any(), any()) } answers { println("Log event") }
        coEvery { productApi.purchaseProducts(any()) } throws mockk<HttpException> {
            every { code() } returns 404
            every { message() } returns "Test message"
        }

        mockkConstructor(Product::class)
        every { anyConstructed<Product>().name } returns "Mocked CD product"

        val result = repository.purchaseProducts(listOf())

        assertThat(result.isFailure).isTrue()

        verify {
            analyticsLogger.logEvent(
                "http_error",
                LogParam("code", 404),
                LogParam("message", "Test message")
            )
        }
    }

    @Test
    fun `Response error, log the error only products with price higher than 5`() = runBlocking {
        coEvery { productApi.purchaseProducts(
            match {
                it.products.any { it.price  > 5 }
            }
        ) } throws mockk<HttpException> {
            every { code() } returns 404
            every { message() } returns "Test message"
        }

        val result = repository.purchaseProducts(
            listOf(
                Product(id = 1, name = "CD", price = 6.0)
            )
        )

        assertThat(result.isFailure).isTrue()

        verify {
            analyticsLogger.logEvent(
                "http_error",
                LogParam("code", 404),
                LogParam("message", "Test message")
            )
        }
    }
}