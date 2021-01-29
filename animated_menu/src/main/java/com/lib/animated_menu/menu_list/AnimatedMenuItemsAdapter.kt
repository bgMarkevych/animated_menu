package com.lib.animated_menu.menu_list

import android.util.SparseArray
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lib.animated_menu.R
import com.lib.animated_menu.item_customizer.ItemsCustomizersProvider

class AnimatedMenuItemsAdapter
    : AbsAnimatedMenuItemsAdapter<AnimatedMenuItemsAdapter.AnimatedMenuItemViewHolder> {

    constructor(
        items: List<MenuItem>,
        customizersProvider: ItemsCustomizersProvider
    ) : super(items) {
        this.customizersProvider = customizersProvider
    }

    constructor(
        items: List<MenuItem>,
        customizersProvider: ItemsCustomizersProvider,
        itemClickListener: AnimatedMenuItemClickListener?
    ) : super(items, itemClickListener) {
        this.customizersProvider = customizersProvider
    }

    init {
        items = items.sortedBy { it.order }
    }

    private val customizersProvider: ItemsCustomizersProvider

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimatedMenuItemViewHolder {
        return AnimatedMenuItemViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.animated_menu_item, parent, false)
        )
    }

    inner class AnimatedMenuItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val icon = itemView.findViewById<ImageView>(R.id.icon)
        val title = itemView.findViewById<TextView>(R.id.title)
    }

    override fun bind(holder: AnimatedMenuItemViewHolder, item: MenuItem) {
        holder.icon.setImageDrawable(item.icon)
        holder.title.text = item.title
        customizersProvider.getCustomizer(item.itemId)?.customize(holder)
    }
}
