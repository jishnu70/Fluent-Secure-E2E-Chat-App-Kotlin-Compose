package com.example.fluent.presentation.userSearch

import com.example.fluent.domain.models.PartnerInfo

data class UserSearchState(
    val isLoggedId: Boolean = false,
    val error: String? = null,
    val userList: List<PartnerInfo> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val searchQuery: String = "",
    val selectedChatID: Int? = null
)
