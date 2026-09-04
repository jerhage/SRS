package com.example.srs.feature.study.review

import com.example.srs.feature.study.card.CardId

interface ReviewLogRepository {
    suspend fun getByCard(cardId: CardId): List<ReviewLog>

    suspend fun append(log: ReviewLog)
}
