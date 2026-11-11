package com.nutmeg.nutmegtest.usecase

import com.nutmeg.nutmegtest.model.Comment

interface GetCommentsUseCaseProtocol {
    suspend fun getComments(postId: Int): Result<List<Comment>>
}
