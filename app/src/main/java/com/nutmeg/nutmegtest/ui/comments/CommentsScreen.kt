package com.nutmeg.nutmegtest.ui.comments

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nutmeg.nutmegtest.model.Comment
import com.nutmeg.nutmegtest.ui.common.PostListItemView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentsScreen(viewModel: CommentsViewModelProtocol) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Comments") }) }
    ) { contentPadding ->
        when (val currentState = state) {
            is CommentsViewState.Loading -> {
                LaunchedEffect(Unit) {
                    viewModel.onTrigger(CommentsViewTrigger.OnAppear)
                }
                Box(
                    modifier = Modifier.fillMaxSize().padding(contentPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is CommentsViewState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(contentPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Error!")
                }
            }

            is CommentsViewState.Loaded -> {
                Column(modifier = Modifier.fillMaxSize().padding(contentPadding)) {
                    PostListItemView(
                        model = currentState.post,
                        showChevron = false,
                        showLikeButton = false,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    HorizontalDivider()

                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(currentState.comments, key = { it.id }) { comment ->
                            CommentItem(comment)
                            HorizontalDivider()
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CommentItem(comment: Comment) {
    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text(
            text = comment.name,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = "(${comment.email})",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = comment.body,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
