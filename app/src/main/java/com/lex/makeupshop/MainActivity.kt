package com.lex.makeupshop

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.snackbar.Snackbar
import com.lex.makeupshop.databinding.ActivityMainBinding
import com.lex.makeupshop.network.ApiClient
import com.lex.makeupshop.network.MakeupItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        viewModel.makeupItemsLiveData.observe(this) { state ->
            processMakeupItemResponse(state)
        }

        viewModel.setupScrollingAppBar(
            binding.appBar, binding.toolbarMainActivity, binding.rvMakeupItems
        )

    }

    private fun processMakeupItemResponse(state: ScreenState<List<MakeupItem>?>){
        when(state){
            is ScreenState.Loading ->{
                binding.progressBar.visibility = View.VISIBLE
            }

            is ScreenState.Success ->{
                binding.progressBar.visibility = View.GONE
                if (state.data != null){
                    //viewModel.makeupItemsLiveData.observe(this) { makeupItem ->
                    val adapter = MainAdapter(this@MainActivity, state.data)
                    binding.rvMakeupItems.layoutManager = LinearLayoutManager(this@MainActivity)
                    binding.rvMakeupItems.adapter = adapter
                    }
                }

            is ScreenState.Error ->{
                binding.progressBar.visibility = View.GONE
                Snackbar.make(binding.root, state.message!!, Snackbar.LENGTH_LONG).show()
            }
        }
    }

}