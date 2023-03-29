package com.lex.makeupshop

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.lex.makeupshop.databinding.RvItemBinding
import com.lex.makeupshop.network.MakeupItem

class MainAdapter(private val context: Context): RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    private var itemList: List<MakeupItem> = emptyList()

    private var onClickListener: OnClickListener? = null

    inner class ViewHolder(private val binding: RvItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bindData(makeupItem: MakeupItem){
            binding.tvName.text = makeupItem.name
            binding.tvBrand.text = makeupItem.brand
            binding.tvPrice.text = makeupItem.price.toString()
            binding.tvPriceSign.text = makeupItem.price_sign
            binding.tvCategory.text = makeupItem.category
            val colorOptionLayoutManager = StaggeredGridLayoutManager(12, StaggeredGridLayoutManager.VERTICAL)
            binding.rvColorOptions.layoutManager = colorOptionLayoutManager
            val adapter = ColorOptionsAdapter(context, makeupItem.product_colors!!)
            binding.rvColorOptions.adapter = adapter

            Glide
                .with(context)
                .load(makeupItem.image)
                .placeholder(R.drawable.item_placeholder)
                .into(binding.ivItemImage)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(RvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(itemList[position])

        holder.itemView.setOnClickListener{
            val intent = Intent(context, ProductDetailsActivity::class.java)
            intent.putExtra(Constants.MAKEUP_ITEM_EXTRA, itemList[position])
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    interface OnClickListener {
        fun onClick(position: Int, makeupItem: MakeupItem)
    }

    fun setOnClickListener(onClickListener: OnClickListener){
        this.onClickListener = onClickListener
    }

    fun updateList(newFilteredList: List<MakeupItem>) {
        itemList = newFilteredList
        Log.e("SIZE: ", itemList.size.toString())
        notifyDataSetChanged()
    }

}