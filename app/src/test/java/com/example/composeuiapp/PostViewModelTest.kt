package com.example.composeuiapp

import com.example.composeuiapp.data.Post
import com.example.composeuiapp.service.APIService
import com.example.composeuiapp.service.APiClient
import com.example.composeuiapp.viewModel.PostViewModel
import io.mockk.coEvery
import kotlinx.coroutines.Dispatchers
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Rule


@OptIn(ExperimentalCoroutinesApi::class)
class PostViewModelTest {

    // Use TestCoroutineDispatcher for testing coroutines
    private val testDispatcher = StandardTestDispatcher()

    private lateinit var viewModel: PostViewModel
    private val mockApiService: APIService = mockk()

    // Allows LiveData to work synchronously in tests
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)  // Set the test dispatcher as the main dispatcher
        viewModel = PostViewModel(mockApiService)  // Inject mocked APIService into ViewModel
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()  // Reset the main dispatcher to the original after test
       // testDispatcher.cleanupTestCoroutines()  // Clean up test coroutines
    }

    @Test
    fun `fetchPosts should update posts state when API call is successful`() = runTest {
        // Arrange: Mock a successful response from the API
        val mockPosts = listOf(
            Post(id = 1, title = "Test Post 1", body = "Body 1"),
            Post(id = 2, title = "Test Post 2", body = "Body 2")
        )
        coEvery { mockApiService.getPosts() } returns mockPosts

        // Act: Observe LiveData and trigger fetchPosts
        val observedPosts = mutableListOf<List<Post>>()
        viewModel.posts.observeForever { observedPosts.add(it) }

        viewModel.fetchPosts()

        // Advance the dispatcher to ensure coroutines execute
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert: Check if the posts state is updated correctly
        assertEquals(mockPosts, observedPosts.last())  // Check last observed value
    }

    @Test
    fun `fetchPosts should handle API call failure`() = runTest {
        // Arrange: Mock an exception thrown by the API
        coEvery { mockApiService.getPosts() } throws Exception("API Error")

        // Act: Observe LiveData and trigger fetchPosts
        val observedPosts = mutableListOf<List<Post>>()
        viewModel.posts.observeForever { observedPosts.add(it) }

        viewModel.fetchPosts()

        // Advance the dispatcher to ensure coroutines execute
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert: Check if the posts state remains empty on failure
        assertEquals(emptyList<Post>(), observedPosts.last())
    }
}


