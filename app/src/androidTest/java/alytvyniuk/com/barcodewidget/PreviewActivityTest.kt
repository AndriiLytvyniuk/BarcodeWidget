package alytvyniuk.com.barcodewidget

import alytvyniuk.com.barcodewidget.model.Barcode
import alytvyniuk.com.barcodewidget.model.Format
import alytvyniuk.com.barcodewidget.model.RawBarcode
import android.graphics.Color
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.junit.Before
import org.junit.Test
import testutils.withColor

class PreviewActivityTest {

    private val testBarcode = Barcode(RawBarcode(Format.CODE_128, "PreviewActivityTest"),
        title = "TestTitle",
        color = Color.BLUE)

    @Before
    fun before() {
        val intent = PreviewActivity.intent(ApplicationProvider.getApplicationContext(), testBarcode)
        ActivityScenario.launch<PreviewActivity>(intent)
    }

    @Test
    fun checkCorrectBarcodeInformation() {
        onView(withId(R.id.dataTextView)).check(matches(withText(testBarcode.rawBarcode.value)))
        onView(withId(R.id.titleTextView)).check(matches(withText(testBarcode.title)))
        onView(withId(R.id.colorFrameView)).check(matches(withColor(testBarcode.color ?: Color.TRANSPARENT)))
    }
}