package finance.pesa.sdk.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Typeface
import android.util.AttributeSet
import android.widget.TextView
import android.text.style.UnderlineSpan
import android.text.SpannableString



@SuppressLint("AppCompatCustomView")
class RoboticMediumUnderLineTextView : TextView {


    constructor(context: Context) : super(context) {
        val face = Typeface.createFromAsset(context.assets, "Roboto_Medium.ttf")
        this.typeface = face
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        val face = Typeface.createFromAsset(context.assets, "Roboto_Medium.ttf")
        this.typeface = face
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        val face = Typeface.createFromAsset(context.assets, "Roboto_Medium.ttf")
        this.typeface = face
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    ) {
        val face = Typeface.createFromAsset(context.assets, "Roboto_Medium.ttf")
        this.typeface = face
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)


    }

    override fun setText(text: CharSequence?, type: BufferType?) {
        val content = SpannableString(text)
        content.setSpan(UnderlineSpan(), 0, content.length, 0)

        super.setText(content, type)
    }

}