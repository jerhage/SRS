package com.example.srs.domain.repository

import com.example.srs.domain.model.CardId
import com.example.srs.domain.model.ReviewLog

interface ReviewLogRepository {
    suspend fun getByCard(cardId: CardId): List<ReviewLog>

    suspend fun append(log: ReviewLog)
}
