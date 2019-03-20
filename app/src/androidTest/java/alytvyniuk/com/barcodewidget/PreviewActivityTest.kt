package alytvyniuk.com.barcodewidget

import alytvyniuk.com.barcodewidget.model.Barcode
import alytvyniuk.com.barcodewidget.model.Format
import alytvyniuk.com.barcodewidget.model.RawBarcode
import android.content.Intent
import android.graphics.Color
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.rule.ActivityTestRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import testutils.withColor

class PreviewActivityTest {

    @get:Rule
    val rule = ActivityTestRule<PreviewActivity>(PreviewActivity::class.java, false, false)

    private val testBarcode = Barcode(RawBarcode(Format.CODE_128, "PreviewActivityTest"), title = "TestTitle", color = Color.BLUE)

    @Before
    fun before() {
        rule.launchActivity(BarcodeActivityHelper.appendExtras(Intent(), testBarcode))
    }

    @Test
    fun checkCorrectBarcodeInformation() {
        onView(withId(R.id.dataTextView)).check(matches(withText(testBarcode.rawBarcode.value)))
        onView(withId(R.id.titleTextView)).check(matches(withText(testBarcode.title)))
        onView(withId(R.id.colorFrameView)).check(matches(withColor(testBarcode.color ?: Color.TRANSPARENT)))
    }
}