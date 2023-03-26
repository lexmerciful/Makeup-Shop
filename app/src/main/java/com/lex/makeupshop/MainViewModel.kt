package com.lex.makeupshop

import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.lex.makeupshop.network.ApiClient
import com.lex.makeupshop.network.MakeupItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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

        _makeupItemsLiveData.postValue(ScreenState.Loading(null))
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val client = repository.getMakeupItems()
                _makeupItemsLiveData.postValue(ScreenState.Success(client))
            }catch (e:Exception){
                _makeupItemsLiveData.postValue(ScreenState.Error(e.message.toString(), null))
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