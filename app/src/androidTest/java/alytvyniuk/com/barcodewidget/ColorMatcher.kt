package alytvyniuk.com.barcodewidget

import android.graphics.drawable.ColorDrawable
import android.view.View
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

fun withColor(color: Int): Matcher<View> {
    return ColorMatcher(color)
}

class ColorMatcher(private val expectedColor : Int) : TypeSafeMatcher<View>() {

    override fun matchesSafely(view: View): Boolean {
        val drawable = view.background
        if (drawable != null && drawable is ColorDrawable) {
            val color = drawable.color
            return color == expectedColor
        }
        return false
    }

    override fun describeTo(description: Description) {
        description.appendText("with expected color: ")
        description.appendText("[")
        description.appendText(expectedColor.toString())
        description.appendText("]")
    }
}