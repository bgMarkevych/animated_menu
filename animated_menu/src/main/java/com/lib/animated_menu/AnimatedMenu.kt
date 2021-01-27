package com.lib.animated_menu

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.util.SparseArray
import android.view.*
import android.widget.FrameLayout
import androidx.appcompat.widget.PopupMenu
import androidx.core.util.set
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lib.animated_menu.animation.AnimationHandler
import com.lib.animated_menu.animation.AnimationListener
import com.lib.animated_menu.animation.AnimationProperties
import com.lib.animated_menu.item_customizer.AnimatedMenuAdapterItemCustomizer
import com.lib.animated_menu.menu_list.AbsAnimatedMenuItemsAdapter
import com.lib.animated_menu.menu_list.AnimatedMenuItemClickListener
import com.lib.animated_menu.menu_list.AnimatedMenuItemsAdapter
import com.lib.animated_menu.utils.values

class AnimatedMenu : FrameLayout, AnimationListener, AnimatedMenuItemClickListener {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : this(context, attrs, defStyleAttr, 0)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        obtainAttributes(context, attrs, defStyleAttr, defStyleRes)
    }

    init {
        val menuLayout = LayoutInflater.from(context).inflate(R.layout.animated_menu, this, false)
        addViewInLayout(menuLayout, 0, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))
    }

    private val animationHandler = AnimationHandler(this)
    private var properties: AnimationProperties? = null
    private var menuItems: SparseArray<MenuItem>? = null
    private var customizers: SparseArray<AnimatedMenuAdapterItemCustomizer>? = null

    var animationDuration: Int = 300

    var isMenuOpened: Boolean = false
        get() = animationHandler.isMenuOpened
        private set

    var menuItemClickListener: AnimatedMenuItemClickListener? = null

    var menuListAdapter: AbsAnimatedMenuItemsAdapter<*>? = null
        set(value) {
            field = value
            value?.itemClickListener = this
            val recycler = findViewById<RecyclerView>(R.id.animated_menu_list)
            recycler.adapter = value
            recycler.layoutManager = LinearLayoutManager(context)
        }

    private fun obtainAttributes(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.AnimatedMenu, defStyleAttr, defStyleRes)

        animationDuration = typedArray.getInteger(R.styleable.AnimatedMenu_animationDuration, 500)

        val headerRes = typedArray.getResourceId(R.styleable.AnimatedMenu_header, 0)
        inflateLayout(headerRes, R.id.animated_menu_header_container)

        val footerRes = typedArray.getResourceId(R.styleable.AnimatedMenu_footer, 0)
        inflateLayout(footerRes, R.id.animated_menu_footer_container)

        val menuRes = typedArray.getResourceId(R.styleable.AnimatedMenu_menu, 0)
        menuItems = parseMenuItems(menuRes)

        if (menuItems != null){
            menuListAdapter = AnimatedMenuItemsAdapter(menuItems!!.values(), customizers, menuItemClickListener)
        }

        setBackgroundColor(typedArray.getColor(R.styleable.AnimatedMenu_backgroundColor, Color.WHITE))

        typedArray.recycle()

    }

    private fun inflateLayout(res: Int, containerRes: Int) {
        if (res == 0) {
            return
        }

        val container = findViewById<ViewGroup>(containerRes)

        val headerLayout = LayoutInflater.from(context).inflate(res, container, false)
        container.addView(headerLayout)
    }

    private fun parseMenuItems(menuRes: Int): SparseArray<MenuItem>? {
        if (menuRes == 0){
            return null
        }
        val popup = PopupMenu(context, this)
        val popupMenu = popup.menu

        MenuInflater(context).inflate(menuRes, popupMenu)

        val menuItems = SparseArray<MenuItem>()

        for (i in 0 until popupMenu.size()) {
            val item = popupMenu.getItem(i)
            menuItems[item.itemId] = item
        }

        return menuItems
    }

    override fun drawChild(canvas: Canvas?, child: View?, drawingTime: Long): Boolean {
        val indexOfChild = indexOfChild(child)

        if (properties != null && indexOfChild == 1) {
            val path = Path()

            val left = properties!!.x + properties!!.corners / 2
            val top = properties!!.y + properties!!.corners
            val right = canvas!!.width.toFloat() + properties!!.x
            val bottom = canvas.height.toFloat() - properties!!.corners

            val rect = RectF(left, top, right, bottom)
            path.addRoundRect(rect, properties!!.corners, properties!!.corners, Path.Direction.CW)
            path.close()

            val save = canvas.save()
            canvas.clipPath(path)
            val result = super.drawChild(canvas, child, drawingTime)
            canvas.restoreToCount(save)
            return result
        }

        return super.drawChild(canvas, child, drawingTime)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        if (childCount > 2) {
            throw IllegalArgumentException("Only 2 children can be specified!")
        }
        super.onLayout(changed, left, top, right, bottom)
    }

    fun setHeader(view: View) {
        findViewById<ViewGroup>(R.id.animated_menu_header_container).addView(view)
    }

    fun setHeaderRes(res: Int) {
        inflateLayout(res, R.id.animated_menu_header_container)
    }

    fun setFooter(view: View) {
        findViewById<ViewGroup>(R.id.animated_menu_footer_container).addView(view)
    }

    fun setFooterRes(res: Int) {
        inflateLayout(res, R.id.animated_menu_footer_container)
    }

    fun customizeItem(itemId: Int, customizer: AnimatedMenuAdapterItemCustomizer) {
        if (customizers == null) {
            customizers = SparseArray()
        }
        customizers!![itemId] = customizer
    }

    fun switchMenu() {
        if (!isMenuOpened) {
            animationHandler.openMenu(getChildAt(1), animationDuration)
            return
        }
        animationHandler.closeMenu(getChildAt(1), animationDuration)
    }

    override fun onAnimation(properties: AnimationProperties) {
        val child = getChildAt(1)

        child?.scaleX = properties.scale
        child?.scaleY = properties.scale

        child?.x = properties.x
        child?.y = properties.y

        this.properties = properties
        invalidate()
    }

    override fun onMenuItemClick(id: Int) {
        animationHandler.closeMenu(getChildAt(1), animationDuration)
        if (menuItemClickListener != null) {
            menuItemClickListener!!.onMenuItemClick(id)
        }
    }

}
