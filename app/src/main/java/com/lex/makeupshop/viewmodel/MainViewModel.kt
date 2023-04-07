package com.lex.makeupshop.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lex.makeupshop.repository.Repository
import com.lex.makeupshop.network.ApiClient
import com.lex.makeupshop.network.MakeupItem
import com.lex.makeupshop.pojo.Brand
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: Repository = Repository(ApiClient.apiService)
) : ViewModel() {

    private var _makeupItemsLiveData = MutableLiveData<ScreenState<List<MakeupItem>?>>()
    val makeupItemsLiveData: LiveData<ScreenState<List<MakeupItem>?>>
    get() = _makeupItemsLiveData;

    private var _brands: MutableList<Brand> = mutableListOf()
    val brand: MutableList<Brand>
    get() = _brands

    init {
        fetchMakeupItems()
    }

    private fun fetchMakeupItems(){

        _makeupItemsLiveData.postValue(ScreenState.Loading(null))
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val client = repository.getMakeupItems()

                // Map out Brands from the List of MakeupItems without repeating brands and ignoring nulls
                _brands = client.mapNotNull { it.brand }.distinct().map { Brand(it) } as MutableList<Brand>

                // Add "All" to brands[0]
                _brands.add(0, Brand("All", true))

                _makeupItemsLiveData.postValue(ScreenState.Success(client))


            }catch (e:Exception){
                _makeupItemsLiveData.postValue(ScreenState.Error(e.message.toString(), null))
            }

        }

    }
}