package com.lib.animated_menu.item_customizer

import androidx.recyclerview.widget.RecyclerView

interface MenuItemCustomizer<in T : RecyclerView.ViewHolder> {

    fun customize(holder: T)

}