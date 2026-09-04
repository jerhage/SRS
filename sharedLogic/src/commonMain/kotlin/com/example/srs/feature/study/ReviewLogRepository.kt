package com.example.srs.feature.study

import com.example.srs.feature.study.model.CardId
import com.example.srs.feature.study.model.ReviewLog

interface ReviewLogRepository {
    suspend fun getByCard(cardId: CardId): List<ReviewLog>

    suspend fun append(log: ReviewLog)
}
