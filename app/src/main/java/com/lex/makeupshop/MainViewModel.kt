package com.lex.makeupshop

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lex.makeupshop.network.ApiClient
import com.lex.makeupshop.network.MakeupItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(
    private val repository: Repository = Repository(ApiClient.apiService)
) : ViewModel() {

    private var _makeupItemsLiveData = MutableLiveData<ScreenState<List<MakeupItem>?>>()
    val makeupItemsLiveData: LiveData<ScreenState<List<MakeupItem>?>>
    get() = _makeupItemsLiveData

    init {
        fetchMakeupItems()
    }

    private fun fetchMakeupItems(){

        val client = repository.getMakeupItems()
        _makeupItemsLiveData.postValue(ScreenState.Loading(null))
        client.enqueue(object : Callback<List<MakeupItem>>{
            override fun onResponse(
                call: Call<List<MakeupItem>>,
                response: Response<List<MakeupItem>>
            ) {
                if (response.isSuccessful){
                    _makeupItemsLiveData.postValue(ScreenState.Success(response.body()))
                }else{
                    _makeupItemsLiveData.postValue(ScreenState.Error(response.code().toString(), null))
                }
            }

            override fun onFailure(call: Call<List<MakeupItem>>, t: Throwable) {
                Log.e("Failure: ", t.message.toString())
                _makeupItemsLiveData.postValue(ScreenState.Error(t.message.toString(), null))
            }

        })
    }
}