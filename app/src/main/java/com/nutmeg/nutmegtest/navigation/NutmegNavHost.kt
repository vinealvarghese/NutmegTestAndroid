package com.nutmeg.nutmegtest.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nutmeg.nutmegtest.api.DefaultNetworker
import com.nutmeg.nutmegtest.api.Host
import com.nutmeg.nutmegtest.model.PostModel
import com.nutmeg.nutmegtest.ui.comments.CommentsScreen
import com.nutmeg.nutmegtest.ui.comments.CommentsViewModel
import com.nutmeg.nutmegtest.ui.posts.PostsScreen
import com.nutmeg.nutmegtest.ui.posts.PostsViewModel
import com.nutmeg.nutmegtest.usecase.GetPostsUseCase

private const val ROUTE_POSTS = "posts"
private const val ROUTE_COMMENTS = "comments"

@Composable
fun NutmegNavHost(navController: NavHostController = rememberNavController()) {
    var selectedPost by remember(navController) {
        mutableStateOf<PostModel?>(null)
    }

    NavHost(navController = navController, startDestination = ROUTE_POSTS) {
        composable(ROUTE_POSTS) {
            val viewModel: PostsViewModel = viewModel(
                factory = PostsViewModel.Factory(
                    GetPostsUseCase(DefaultNetworker(Host.JSON_API))
                )
            )
            PostsScreen(
                viewModel = viewModel,
                onPostSelected = { post ->
                    selectedPost = post
                    navController.navigate(ROUTE_COMMENTS)
                }
            )
        }
        composable(ROUTE_COMMENTS) {
            val post = selectedPost ?: return@composable
            val viewModel: CommentsViewModel = viewModel(
                factory = CommentsViewModel.Factory(
                    post,
                    DefaultNetworker(Host.JSON_API)
                )
            )
            CommentsScreen(viewModel = viewModel)
        }
    }
}
