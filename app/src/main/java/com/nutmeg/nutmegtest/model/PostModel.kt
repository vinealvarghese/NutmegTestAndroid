package com.nutmeg.nutmegtest.model

data class PostModel(
    val post: Post,
    val user: User
) {
    val id: Int get() = post.id
}
