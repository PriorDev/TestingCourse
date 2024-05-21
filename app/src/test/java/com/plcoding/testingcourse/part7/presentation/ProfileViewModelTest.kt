@file:OptIn(ExperimentalCoroutinesApi::class)

package com.plcoding.testingcourse.part7.presentation

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isNull
import assertk.assertions.isTrue
import com.plcoding.testingcourse.part7.data.UserRepositoryFake
import com.plcoding.testingcourse.util.MainCoroutineExtension
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.Dispatcher
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MainCoroutineExtension::class)
class ProfileViewModelTest {
    private lateinit var viewModel: ProfileViewModel
    private lateinit var repository: UserRepositoryFake

    @BeforeEach
    fun setUp() {
        repository = UserRepositoryFake()
        viewModel = ProfileViewModel(
            repository,
            SavedStateHandle(
                initialState = mapOf(
                    "userId" to repository.profileToReturn.user.id
                )
            )
        )
    }

    @Test
    fun `Test loading profile success`() = runTest {
        viewModel.loadProfile()

        advanceUntilIdle()

        assertThat(viewModel.state.value.profile).isEqualTo(repository.profileToReturn)
        assertThat(viewModel.state.value.isLoading).isFalse()
    }

    @Test
    fun `Test loading error`() = runTest {
        repository.errorToReturn = Exception("Test exception")
        viewModel.loadProfile()

        advanceUntilIdle()

        assertThat(viewModel.state.value.profile).isNull()
        assertThat(viewModel.state.value.errorMessage).isEqualTo("Test exception")
        assertThat(viewModel.state.value.isLoading).isFalse()
    }

    @Test
    fun `Test loasing state update`() = runTest {
        viewModel.state.test {
            val emision1 = awaitItem()
            assertThat(emision1.isLoading).isFalse()

            viewModel.loadProfile()

            val emision2 = awaitItem()
            assertThat(emision2.isLoading).isTrue()

            val emision3 = awaitItem()
            assertThat(emision3.isLoading).isFalse()
        }
    }
}