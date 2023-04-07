package com.lex.makeupshop.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.lex.makeupshop.pojo.Brand
import com.lex.makeupshop.adapters.MainAdapter
import com.lex.makeupshop.viewmodel.MainViewModel
import com.lex.makeupshop.viewmodel.ScreenState
import com.lex.makeupshop.adapters.BrandAdapter
import com.lex.makeupshop.databinding.ActivityMainBinding
import com.lex.makeupshop.network.MakeupItem

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

        setupScrollingBar(
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

    fun setupScrollingBar(appBarLayout: AppBarLayout, toolbar: Toolbar, fab: FloatingActionButton, recyclerView: RecyclerView){
        val layoutParams = toolbar.layoutParams as AppBarLayout.LayoutParams
        layoutParams.scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS
        toolbar.layoutParams = layoutParams

        val behavior = appBarLayout.behavior as AppBarLayout.Behavior?
        if (behavior != null && behavior is AppBarLayout.Behavior) {
            behavior.setDragCallback(object : AppBarLayout.Behavior.DragCallback() {
                override fun canDrag(appBarLayout: AppBarLayout): Boolean {
                    // Enable dragging of the toolbar only when the RecyclerView is at the top
                    return recyclerView.computeVerticalScrollOffset() == 0
                }
            })
        }


        // Add an OnScrollListener to the RecyclerView
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                // Show/hide the FloatingActionButton based on the scrolling direction
                if (dy > 0 && fab.visibility != View.VISIBLE) {
                    // Scroll down - show the FloatingActionButton
                    fab.show()
                } else if (dy < 0 && fab.visibility == View.VISIBLE) {
                    // Scroll up - hide the FloatingActionButton
                    fab.hide()
                }
            }
        })

        fab.setOnClickListener {
            recyclerView.smoothScrollToPosition(0)
        }
    }

}