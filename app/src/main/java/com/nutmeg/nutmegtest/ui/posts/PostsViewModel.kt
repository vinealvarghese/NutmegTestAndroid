package com.nutmeg.nutmegtest.ui.posts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nutmeg.nutmegtest.model.PostModel
import com.nutmeg.nutmegtest.usecase.GetPostsUseCaseProtocol
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class PostsViewState {
    object Loading : PostsViewState()
    object Error : PostsViewState()
    data class Loaded(val models: List<PostModel>) : PostsViewState()
}

sealed class PostsViewTrigger {
    object OnAppear : PostsViewTrigger()
}

interface PostsViewModelProtocol {
    val state: StateFlow<PostsViewState>
    fun onTrigger(trigger: PostsViewTrigger)
}

class PostsViewModel(
    private val getPostsUseCase: GetPostsUseCaseProtocol
) : ViewModel(), PostsViewModelProtocol {

    private val _state = MutableStateFlow<PostsViewState>(PostsViewState.Loading)
    override val state: StateFlow<PostsViewState> = _state

    override fun onTrigger(trigger: PostsViewTrigger) {
        when (trigger) {
            is PostsViewTrigger.OnAppear -> getData()
        }
    }

    private fun getData() {
        viewModelScope.launch {
            val result = getPostsUseCase.getPosts()
            _state.value = result.fold(
                onSuccess = { PostsViewState.Loaded(it) },
                onFailure = { PostsViewState.Error }
            )
        }
    }

    class Factory(
        private val getPostsUseCase: GetPostsUseCaseProtocol
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            PostsViewModel(getPostsUseCase) as T
    }
}
