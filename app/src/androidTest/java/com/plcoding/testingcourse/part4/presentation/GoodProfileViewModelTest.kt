package com.plcoding.testingcourse.part4.presentation

import com.plcoding.testingcourse.part1.domain.AnalyticsLogger
import com.plcoding.testingcourse.part1.domain.LogParams
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GoodProfileViewModelTest {
    private lateinit var viewModel: GoodProfileViewModel

    @BeforeEach
    fun setUp() {
        viewModel = GoodProfileViewModel(
            analytics = object : AnalyticsLogger {
                override fun logEvent(key: String, vararg params: LogParams<Any>) = Unit

            }
        )
    }
    @Test
    fun saveProfile() {
    }
}