package com.lib.animated_menu.item_customizer

import android.util.SparseArray

interface ItemsCustomizersProvider {

    fun getCustomizers(): SparseArray<AnimatedMenuAdapterItemCustomizer>?
    fun getCustomizer(id: Int): AnimatedMenuAdapterItemCustomizer?

}