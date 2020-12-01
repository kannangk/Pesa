package finance.pesa.sdk.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import finance.pesa.sdk.R

class NavigationIcons : LinearLayout {
    private var mTextView: TextView? = null
    private var main_lay: LinearLayout? = null
    private var mImageView: ImageView? = null
    private var active: Boolean = false
    private var unSelectedImage: Int? = null
    private var selectedImage: Int? = null

    constructor(context: Context, title: String, drawableNormal: Int, drawablePressed: Int) : super(
        context
    ) {
        init(context, title, drawableNormal, drawablePressed, false, false)
    }

    constructor(
        context: Context,
        title: String,
        drawableNormal: Int,
        drawablePressed: Int,
        isActive: Boolean,
        isBadge: Boolean
    ) : super(context) {
        init(context, title, drawableNormal, drawablePressed, isActive, isBadge)
    }

    constructor(
        context: Context,
        title: String,
        drawableNormal: Int,
        drawablePressed: Int,
        isActive: Boolean,
        credits: String
    ) : super(context) {
        init(context, title, drawableNormal, drawablePressed, isActive, credits)
    }

    constructor(
        context: Context,
        title: String,
        drawableNormal: Int,
        drawablePressed: Int,
        isActive: Boolean,
        credits: String,
        connect: String
    ) : super(context) {
        init(context, title, drawableNormal, drawablePressed, isActive, credits, connect)
    }

    private fun init(
        context: Context,
        title: String,
        drawableNormal: Int,
        drawablePressed: Int,
        isActive: Boolean,
        isBadge: Boolean
    ) {
        this.active = isActive
        LayoutInflater.from(context).inflate(R.layout.layout_bottom_icon, this)
        mTextView = findViewById(R.id.textNavigation)
        mImageView = findViewById(R.id.imageNavigation)
        main_lay = findViewById(R.id.main_lay)
        /* mTextView!!.setTextColor(context.getColor(R.color.bottom_icon_text))
         mTextView!!.visibility = View.GONE
         mTextView!!.text = title*/
        unSelectedImage = drawableNormal
        selectedImage = drawablePressed
        mImageView!!.setImageResource(selectedImage!!)
        val layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
        setLayoutParams(layoutParams)

    }

    private fun init(
        context: Context,
        title: String,
        drawableNormal: Int,
        drawablePressed: Int,
        isActive: Boolean,
        credits: String
    ) {
        this.active = isActive
        LayoutInflater.from(context).inflate(R.layout.layout_bottom_icon, this)
        mTextView = findViewById(R.id.textNavigation)
        mImageView = findViewById(R.id.imageNavigation)
        main_lay = findViewById(R.id.main_lay)
        mTextView!!.visibility = View.GONE
        mTextView!!.setTextColor(context.getColor(R.color.bottom_icon_text))
        mTextView!!.text = title
        unSelectedImage = drawableNormal
        selectedImage = drawablePressed
        mImageView!!.setImageResource(selectedImage!!)
        // main_lay!!.setBackgroundColor(context.getColor(R.color.white))
        val layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
        setLayoutParams(layoutParams)

    }

    private fun init(
        context: Context,
        title: String,
        drawableNormal: Int,
        drawablePressed: Int,
        isActive: Boolean,
        credits: String,
        connect: String
    ) {
        this.active = isActive
        LayoutInflater.from(context).inflate(R.layout.layout_bottom_icon, this)
        mTextView = findViewById(R.id.textNavigation)
        mImageView = findViewById(R.id.imageNavigation)
        main_lay = findViewById(R.id.main_lay)
        mImageView!!.visibility = View.GONE
        mTextView!!.setTextColor(context.getColor(R.color.bottom_icon_text))
        mTextView!!.text = title
        unSelectedImage = drawableNormal
        selectedImage = drawablePressed
        mImageView!!.setImageResource(selectedImage!!)
        val layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
        setLayoutParams(layoutParams)

    }

    fun setState(tabIconColor: Int, bgColor: Int) {
        main_lay!!.setBackgroundColor(context.getColor(bgColor))
        mImageView!!.setColorFilter(
            ContextCompat.getColor(context, tabIconColor),
            android.graphics.PorterDuff.Mode.MULTIPLY
        )

    }

}
