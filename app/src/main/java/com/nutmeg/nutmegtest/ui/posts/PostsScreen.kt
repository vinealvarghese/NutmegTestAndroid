package com.nutmeg.nutmegtest.ui.posts

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nutmeg.nutmegtest.model.PostModel
import com.nutmeg.nutmegtest.ui.common.PostListItemView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostsScreen(
    viewModel: PostsViewModelProtocol,
    onPostSelected: (PostModel) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Posts") }) }
    ) { contentPadding ->
        when (val currentState = state) {
            is PostsViewState.Loading -> {
                LaunchedEffect(Unit) {
                    viewModel.onTrigger(PostsViewTrigger.OnAppear)
                }
                Box(
                    modifier = Modifier.fillMaxSize().padding(contentPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is PostsViewState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(contentPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Error!")
                }
            }

            is PostsViewState.Loaded -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(contentPadding),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(currentState.models, key = { it.id }) { model ->
                        PostListItemView(
                            model = model,
                            showChevron = true,
                            showLikeButton = false,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onPostSelected(model) }
                                .padding(vertical = 8.dp)
                        )
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}
