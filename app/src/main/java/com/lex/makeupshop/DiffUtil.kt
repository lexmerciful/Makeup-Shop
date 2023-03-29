package com.lex.makeupshop

import androidx.recyclerview.widget.DiffUtil
import com.lex.makeupshop.network.MakeupItem

class DiffUtil(
    private val oldList: List<MakeupItem>,
    private val newList: List<MakeupItem>
): DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].name == newList[newItemPosition].name
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList == newList
    }
}