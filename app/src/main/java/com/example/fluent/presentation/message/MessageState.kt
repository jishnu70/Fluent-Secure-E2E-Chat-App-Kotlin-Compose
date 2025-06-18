package com.example.fluent.presentation.message

import com.example.fluent.domain.models.Message
import com.example.fluent.domain.models.PartnerInfo

data class MessageState(
    val partnerInfo: PartnerInfo? = null,
    val error: String? = null,
    val isLoading: Boolean = false,
    val messages: List<Message> = emptyList(),
    val messageString: String = "",
)
