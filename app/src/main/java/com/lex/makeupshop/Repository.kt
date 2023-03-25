package com.lex.makeupshop

import com.lex.makeupshop.network.ApiService

class Repository(private val apiService: ApiService) {
    fun getMakeupItems() = apiService.fetchMakeupItems()
}