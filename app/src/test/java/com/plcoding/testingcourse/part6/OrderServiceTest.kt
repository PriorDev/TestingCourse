package com.plcoding.testingcourse.part6

import assertk.assertThat
import com.google.firebase.auth.FirebaseAuth
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class OrderServiceTest {
    private lateinit var auth: FirebaseAuth
    private lateinit var emailClient: EmailClient
    private lateinit var orderService: OrderService

    @BeforeEach
    fun setUp() {
        auth = mockk()
        emailClient = mockk(relaxed = true)

        orderService = OrderService(auth, emailClient)
    }

    @Test
    fun `Test non anonymous user send correct email`() {
        every { auth.currentUser?.isAnonymous } returns false

        val testEmail = "test@email.com"
        val productName = "Cookies"
        orderService.placeOrder(testEmail, productName)

        verify {
            emailClient.send(
                Email(
                    subject = "Order Confirmation",
                    content = "Thank you for your order of $productName.",
                    recipient = testEmail
                )
            )
        }
    }
}