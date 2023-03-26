package com.lex.makeupshop

import com.lex.makeupshop.network.ApiService

class Repository(private val apiService: ApiService) {
    suspend fun getMakeupItems() = apiService.fetchMakeupItems()
}