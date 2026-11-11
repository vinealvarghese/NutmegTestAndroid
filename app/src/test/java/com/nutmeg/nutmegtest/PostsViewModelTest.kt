package com.nutmeg.nutmegtest

import com.nutmeg.nutmegtest.model.Post
import com.nutmeg.nutmegtest.model.PostModel
import com.nutmeg.nutmegtest.model.User
import com.nutmeg.nutmegtest.ui.posts.PostsViewModel
import com.nutmeg.nutmegtest.ui.posts.PostsViewState
import com.nutmeg.nutmegtest.ui.posts.PostsViewTrigger
import com.nutmeg.nutmegtest.usecase.GetPostsUseCaseProtocol
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PostsViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    private lateinit var sut: PostsViewModel
    private lateinit var mock: MockGetPostsUseCase

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        mock = MockGetPostsUseCase()
        sut = PostsViewModel(mock)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testLoadData() = runTest {
        sut.onTrigger(PostsViewTrigger.OnAppear)
        dispatcher.scheduler.advanceUntilIdle()

        when (val state = sut.state.value) {
            is PostsViewState.Loaded -> assertEquals(1, state.models.size)
            else -> fail("Should have loaded data")
        }
    }

    @Test
    fun testError() = runTest {
        mock.returnError = true
        sut.onTrigger(PostsViewTrigger.OnAppear)
        dispatcher.scheduler.advanceUntilIdle()

        when (sut.state.value) {
            is PostsViewState.Error -> Unit
            else -> fail("Should have returned an error")
        }
    }
}

class MockGetPostsUseCase : GetPostsUseCaseProtocol {

    private val posts: List<PostModel> = listOf(
        PostModel(
            post = Post(id = 1, userId = 1, title = "Title", body = "Body"),
            user = User(id = 1, username = "Username")
        )
    )

    var returnError: Boolean = false

    override suspend fun getPosts(): Result<List<PostModel>> =
        if (returnError) {
            Result.failure(com.nutmeg.nutmegtest.api.NetworkError.Generic)
        } else {
            Result.success(posts)
        }
}
