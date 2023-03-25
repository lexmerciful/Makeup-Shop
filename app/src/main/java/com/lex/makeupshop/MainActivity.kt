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


        /*val client = ApiClient.apiService.fetchMakeupItems()

        client.enqueue(object : Callback<List<MakeupItem>> {
            override fun onResponse(
                call: Call<List<MakeupItem>>,
                response: Response<List<MakeupItem>>
            ) {
                if (response.isSuccessful){
                    Log.e("Product:", ""+response.body())

                    val result = response.body()
                    result?.let {
                        val adapter = MainAdapter(this@MainActivity, result)

                        binding.rvMakeupItems.layoutManager = LinearLayoutManager(this@MainActivity)
                        binding.rvMakeupItems.adapter = adapter

                        adapter.setOnClickListener(object : MainAdapter.OnClickListener{
                            override fun onClick(position: Int, makeupItem: MakeupItem) {
                                val intent = Intent(this@MainActivity, ProductDetailsActivity::class.java)
                                intent.putExtra(MAKEUP_ITEM_EXTRA, makeupItem)
                                startActivity(intent)
                            }

                        })
                    }
                }
            }

            override fun onFailure(call: Call<List<MakeupItem>>, t: Throwable) {
                Log.e("Failed:", ""+t.message)
            }

        })*/

        val appBarLayout: AppBarLayout = binding.appBar
        val toolbar: Toolbar = binding.toolbarMainActivity
        setSupportActionBar(toolbar)
        val layoutParams = toolbar.layoutParams as AppBarLayout.LayoutParams
        layoutParams.scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS
        toolbar.layoutParams = layoutParams

        val behavior = appBarLayout.behavior as AppBarLayout.Behavior?
        if (behavior != null && behavior is AppBarLayout.Behavior) {
            behavior.setDragCallback(object : AppBarLayout.Behavior.DragCallback() {
                override fun canDrag(appBarLayout: AppBarLayout): Boolean {
                    // Enable dragging of the toolbar only when the RecyclerView is at the top
                    return binding.rvMakeupItems.computeVerticalScrollOffset() == 0
                }
            })
        }

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