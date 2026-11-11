package com.nutmeg.nutmegtest.usecase

interface GetPostImageUseCaseProtocol {
    fun hasImage(postId: Int): Boolean
}

class GetPostImageUseCase : GetPostImageUseCaseProtocol {
    override fun hasImage(postId: Int): Boolean = false
}
