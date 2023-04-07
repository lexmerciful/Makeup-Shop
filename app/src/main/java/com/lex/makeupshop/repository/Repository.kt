package com.lex.makeupshop.repository

import com.lex.makeupshop.network.ApiService

class Repository(private val apiService: ApiService) {
    suspend fun getMakeupItems() = apiService.fetchMakeupItems()
}