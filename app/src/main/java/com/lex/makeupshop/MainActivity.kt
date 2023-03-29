package com.lex.makeupshop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
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

    private lateinit var selectedBrandNames: List<String?>
    private lateinit var filteredMakeupItems: List<MakeupItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        viewModel.makeupItemsLiveData.observe(this) { state ->
            processMakeupItemResponse(state)
        }

        viewModel.setupScrollingBar(
            binding.appBar, binding.toolbarMainActivity, binding.fabScrollUp, binding.rvMakeupItems
        )

    }

    private fun processMakeupItemResponse(state: ScreenState<List<MakeupItem>?>){
        when(state){
            is ScreenState.Loading ->{
                binding.progressBar.visibility = View.VISIBLE
            }

            is ScreenState.Success ->{
                val brands = viewModel.brand

                binding.progressBar.visibility = View.GONE
                if (state.data != null){
                    //viewModel.makeupItemsLiveData.observe(this) { makeupItem ->
                    val adapter = MainAdapter(this@MainActivity)
                    adapter.updateList(state.data)
                    binding.rvMakeupItems.layoutManager = LinearLayoutManager(this@MainActivity)
                    binding.rvMakeupItems.adapter = adapter

                    // Setup Brand Adapter
                    val brandAdapter = BrandAdapter(this@MainActivity, brands, binding.rvBrandList)
                    binding.rvBrandList.layoutManager = LinearLayoutManager(this@MainActivity,LinearLayoutManager.HORIZONTAL,false)
                    binding.rvBrandList.adapter = brandAdapter

                    brandAdapter.setOnClickListener(object : BrandAdapter.OnClickListener{
                        override fun onClick(position: Int, brand: Brand) {
                            // Unselect all other brands
                            brands.filter { it.isSelected }.forEach { it.isSelected = false }

                            // Select the clicked brand
                            brand.isSelected = true

                            // Filter MakeupList based on brand selected
                            if (brand.name == "All"){
                                filteredMakeupItems = state.data
                            }else {
                                selectedBrandNames = brands.filter { it.isSelected }.map { it.name }
                                filteredMakeupItems = state.data.filter { it.brand in selectedBrandNames }
                            }

                            //Update MakeupList Adapter based on FilteredList
                            adapter.updateList(filteredMakeupItems)
                            binding.rvMakeupItems.scrollToPosition(0)
                            Log.e(brand.name, filteredMakeupItems.size.toString())
                        }

                    })

                    }
                }

            is ScreenState.Error ->{
                binding.progressBar.visibility = View.GONE
                Snackbar.make(binding.root, state.message!!, Snackbar.LENGTH_LONG).show()
            }
        }
    }

}