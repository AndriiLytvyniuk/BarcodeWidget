package alytvyniuk.com.barcodewidget

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.ImageView


class ButtonColorPicker : View.OnClickListener {

    private val paddingSize = 15
    private var toggledView: ImageView? = null
    private var onColorListener : OnColorListener? = null

    fun setOnColorListener(onColorListener: OnColorListener) : ButtonColorPicker {
        this.onColorListener = onColorListener
        return this
    }

    fun addView(imageView: ImageView) {
        addView(imageView, false)
    }

    fun addView(imageView: ImageView, makeToggled: Boolean) {
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
            return (it.drawable as ColorDrawable).color
        }
        return Color.TRANSPARENT
    }

    override fun onClick(v: View) {
        unchooseToggledView()
        choose(v)
        toggledView = v as ImageView
        onColorListener?.onColorSelected(getCurrentColor())
    }

    private fun unchoose(v : View) {
        v.isEnabled = true
        v.setPadding(0, 0, 0, 0)
    }

    private fun unchooseToggledView() {
        toggledView?.let {
            unchoose(it)
        }
    }

    private fun choose(v : View) {
        v.isEnabled = false
        val p = paddingSize
        v.setPadding(p, p, p, p)
    }

    private fun isChosen(v : View) = !v.isEnabled

}

interface OnColorListener {

    fun onColorSelected(color: Int)
}