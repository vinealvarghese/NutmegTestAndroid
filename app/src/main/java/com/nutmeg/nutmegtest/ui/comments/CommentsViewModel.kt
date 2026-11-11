package com.nutmeg.nutmegtest.ui.comments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nutmeg.nutmegtest.api.NetworkingPath
import com.nutmeg.nutmegtest.api.Networker
import com.nutmeg.nutmegtest.model.Comment
import com.nutmeg.nutmegtest.model.PostModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class CommentsViewState {
    object Loading : CommentsViewState()
    object Error : CommentsViewState()
    data class Loaded(val post: PostModel, val comments: List<Comment>) : CommentsViewState()
}

sealed class CommentsViewTrigger {
    object OnAppear : CommentsViewTrigger()
}

interface CommentsViewModelProtocol {
    val state: StateFlow<CommentsViewState>
    fun onTrigger(trigger: CommentsViewTrigger)
}

class CommentsViewModel(
    private val post: PostModel,
    private val networker: Networker
) : ViewModel(), CommentsViewModelProtocol {

    private val _state = MutableStateFlow<CommentsViewState>(CommentsViewState.Loading)
    override val state: StateFlow<CommentsViewState> = _state

    override fun onTrigger(trigger: CommentsViewTrigger) {
        when (trigger) {
            is CommentsViewTrigger.OnAppear -> getComments()
        }
    }

    private fun getComments() {
        viewModelScope.launch {
            val result = networker.getModel(Array<Comment>::class.java, NetworkingPath.Comments)
            _state.value = result.fold(
                onSuccess = { comments ->
                    CommentsViewState.Loaded(post, comments.filter { it.postId == post.id })
                },
                onFailure = { CommentsViewState.Error }
            )
        }
    }

    class Factory(
        private val post: PostModel,
        private val networker: Networker
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            CommentsViewModel(post, networker) as T
    }
}
