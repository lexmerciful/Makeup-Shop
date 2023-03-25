package com.lex.makeupshop

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lex.makeupshop.databinding.RvColorItemsBinding
import com.lex.makeupshop.network.ProductColors

class ColorOptionsAdapter(private val context: Context,
                          private val itemColorList: List<ProductColors>): RecyclerView.Adapter<ColorOptionsAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: RvColorItemsBinding): RecyclerView.ViewHolder(binding.root){
        fun bindItem(productColors: ProductColors){
            try {
                binding.tvColorName.visibility = View.GONE
                binding.viewColor.setBackgroundColor(Color.parseColor(productColors.hex_value))
            }catch (e:Exception){
               Log.e("Color Error: ", e.message.toString())
            }
            if (context is ProductDetailsActivity){
                binding.tvColorName.visibility = View.VISIBLE
                binding.tvColorName.text = productColors.colour_name
            }

        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ColorOptionsAdapter.ViewHolder {
        return ViewHolder(RvColorItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ColorOptionsAdapter.ViewHolder, position: Int) {
        holder.bindItem(itemColorList[position])
    }

    override fun getItemCount(): Int {
        return itemColorList.size
    }
}