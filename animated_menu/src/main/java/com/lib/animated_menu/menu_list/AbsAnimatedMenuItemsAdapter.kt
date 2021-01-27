package com.lib.animated_menu.menu_list

import android.view.MenuItem
import androidx.recyclerview.widget.RecyclerView

abstract class AbsAnimatedMenuItemsAdapter<T : RecyclerView.ViewHolder> : RecyclerView.Adapter<T> {
    constructor(items: List<MenuItem>) : this(items, null)
    constructor(items: List<MenuItem>, itemClickListener: AnimatedMenuItemClickListener?) {
        this.items = items
        this.itemClickListener = itemClickListener
    }

    protected var items: List<MenuItem>
    var itemClickListener: AnimatedMenuItemClickListener? = null

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: T, position: Int) {
        holder.itemView.setOnClickListener {
            itemClickListener?.onMenuItemClick(items[position].itemId)
        }
        bind(holder, items[position])
    }

    abstract fun bind(holder: T, item: MenuItem)

}