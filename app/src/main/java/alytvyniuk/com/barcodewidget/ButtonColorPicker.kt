package alytvyniuk.com.barcodewidget

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.ImageView


class ButtonColorPicker : View.OnClickListener {

    //private val paddingSize = 5
    private var toggledView: ImageView? = null
    private var onColorListener : OnColorListener? = null

    fun setOnColorListener(onColorListener: OnColorListener) : ButtonColorPicker {
        this.onColorListener = onColorListener
        return this
    }

    fun addView(imageView: ImageView, color: Int, makeToggled: Boolean) {
        imageView.drawable.colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP)
        imageView.tag = color
        unchoose(imageView)
        if (makeToggled) {
            choose(imageView)
            unchooseToggledView()
            toggledView = imageView
        }
        imageView.setOnClickListener(this)
    }

    fun getCurrentColor() : Int {
        toggledView?.let {
            return it.tag as Int
        }
        return Color.TRANSPARENT
    }

    override fun onClick(v: View) {
        unchooseToggledView()
        choose(v)
        toggledView = v as ImageView
        onColorListener?.onColorSelected(getCurrentColor())
    }

    private fun unchoose(v : ImageView) {
        v.isEnabled = true
        v.background.colorFilter = PorterDuffColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP)
    }

    private fun unchooseToggledView() {
        toggledView?.let {
            unchoose(it)
        }
    }

    private fun choose(v : View) {
        v.isEnabled = false
        v.background.colorFilter = PorterDuffColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP)
        //val p = paddingSize
        //v.setPadding(p, p, p, p)
    }

    private fun isChosen(v : View) = !v.isEnabled

}

interface OnColorListener {

    fun onColorSelected(color: Int)
}