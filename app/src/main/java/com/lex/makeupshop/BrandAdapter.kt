package com.lex.makeupshop

import android.content.Context
import android.graphics.Typeface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.lex.makeupshop.databinding.ItemBrandTagBinding
import com.lex.makeupshop.network.MakeupItem

class BrandAdapter(private val context: Context, private val brandList: List<Brand>, private val recyclerView: RecyclerView) :
    RecyclerView.Adapter<BrandAdapter.ViewHolder>() {

    private var onClickListener: OnClickListener? = null

    var selectedItemPosition: Int = 0

    inner class ViewHolder(val binding: ItemBrandTagBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Brand) {
            binding.tvBrandName.text = item.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemBrandTagBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val brand = brandList[position]
        holder.bind(brand)

        if (brand.isSelected) {
            holder.binding.cvProductColor.setBackgroundResource(R.drawable.shape_brand_selected_bg)
            holder.binding.tvBrandName.setTextColor(ContextCompat.getColor(context, R.color.white))
        } else {
            holder.binding.cvProductColor.setBackgroundResource(R.drawable.shape_brand_unselected_bg)
            holder.binding.tvBrandName.setTextColor(ContextCompat.getColor(context, R.color.black))
        }

        holder.itemView.setOnClickListener {
            if (holder.adapterPosition != 0){
                holder.binding.cvProductColor.setBackgroundResource(R.drawable.shape_brand_unselected_bg)
                holder.binding.tvBrandName.setTextColor(ContextCompat.getColor(context, R.color.black))
            }
            onClickListener!!.onClick(position, brand)
            if (onClickListener != null) {
                try {
                    val previousSelectedItemPosition = selectedItemPosition
                    selectedItemPosition = holder.adapterPosition

                    // Update the background of the selected item
                    holder.binding.root.setBackgroundResource(R.drawable.shape_brand_selected_bg)
                    holder.binding.tvBrandName.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.white
                        )
                    )

                    // Update the background of the previously selected item
                    if (previousSelectedItemPosition != RecyclerView.NO_POSITION) {
                        val previousSelectedItemView =
                            recyclerView.findViewHolderForAdapterPosition(
                                previousSelectedItemPosition
                            )?.itemView
                        //previousSelectedItemView?.setBackgroundResource(R.drawable.shape_brand_unselected_bg)
                        val previousBinding = ItemBrandTagBinding.bind(previousSelectedItemView!!)
                        previousBinding.root.setBackgroundResource(R.drawable.shape_brand_unselected_bg)
                        previousBinding.tvBrandName.setTextColor(
                            ContextCompat.getColor(
                                previousSelectedItemView.context,
                                R.color.black
                            )
                        )
                    }
                }catch (e:Exception){
            Log.e("ERROR: ", e.message.toString())
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return brandList.size
    }

    interface OnClickListener {
        fun onClick(position: Int, brand: Brand)
    }

    fun setOnClickListener(onClickListener: OnClickListener){
        this.onClickListener = onClickListener
    }

}