package com.nutmeg.nutmegtest.usecase

import com.nutmeg.nutmegtest.api.NetworkingPath
import com.nutmeg.nutmegtest.api.Networker
import com.nutmeg.nutmegtest.model.Post
import com.nutmeg.nutmegtest.model.PostModel
import com.nutmeg.nutmegtest.model.User

interface GetPostsUseCaseProtocol {
    suspend fun getPosts(): Result<List<PostModel>>
}

class GetPostsUseCase(private val networker: Networker) : GetPostsUseCaseProtocol {

    override suspend fun getPosts(): Result<List<PostModel>> {
        val postsResult = networker.getModel(Array<Post>::class.java, NetworkingPath.Posts)
        val posts = postsResult.getOrElse { return Result.failure(it) }

        val usersResult = networker.getModel(Array<User>::class.java, NetworkingPath.Users)
        val users = usersResult.getOrElse { return Result.failure(it) }

        return Result.success(mapToModels(posts.toList(), users.toList()))
    }

    private fun mapToModels(posts: List<Post>, users: List<User>): List<PostModel> =
        posts.map { post ->
            PostModel(
                post = post,
                user = users.firstOrNull { it.id == post.userId } ?: User(id = -1, username = "")
            )
        }
}
