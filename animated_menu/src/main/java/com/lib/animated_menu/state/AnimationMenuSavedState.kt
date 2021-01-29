package com.lib.animated_menu.state

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import android.view.View
import androidx.annotation.RequiresApi

class AnimationMenuSavedState : View.BaseSavedState {

    constructor(superState: Parcelable?) : super(superState)

    @RequiresApi(Build.VERSION_CODES.N)
    constructor(parcel: Parcel, classLoader: ClassLoader) : super(parcel, classLoader)
    constructor(parcel: Parcel) : super(parcel) {
        isOpened = parcel.readInt() == 1
    }

    var isOpened = false

    override fun writeToParcel(out: Parcel?, flags: Int) {
        super.writeToParcel(out, flags)
        out?.writeInt(if (isOpened) 1 else 0)
    }

}