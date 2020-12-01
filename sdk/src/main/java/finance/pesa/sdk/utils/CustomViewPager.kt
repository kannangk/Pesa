package finance.pesa.sdk.utils

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

class CustomViewPager : ViewPager {
    private var enabled: Boolean? = false


    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        this.enabled = true
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return enabled!! && super.onTouchEvent(event)
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        return enabled!! && super.onInterceptTouchEvent(event)
    }

    fun setPagingEnabled(enabled: Boolean) {
        this.enabled = enabled
    }

    fun isPagingEnabled(): Boolean {
        return enabled!!
    }
}