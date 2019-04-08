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
import org.junit.Test
import testutils.withColor

private const val WIDGET_ID = 1

class EditActivityTest {

    private val testBarcode = Barcode(
        RawBarcode(Format.QR_CODE, "first"),
        title = "TestTitle",
        color = Color.RED
    )

    private val testBarcodeTemplate = Barcode(RawBarcode(Format.QR_CODE, "second"))

    private fun launchAsEdit() : ActivityScenario<EditActivity> {
        val intent = EditActivity.intent(ApplicationProvider.getApplicationContext(), testBarcode, WIDGET_ID)
        return ActivityScenario.launch<EditActivity>(intent)
    }

    private fun launchAsSave() : ActivityScenario<EditActivity> {
        val intent = EditActivity.intent(ApplicationProvider.getApplicationContext(), testBarcodeTemplate)
        return ActivityScenario.launch<EditActivity>(intent)
    }

    @Test
    fun checkCorrectBarcodeInformationWhenEdit() {
        launchAsEdit()
        onView(withId(R.id.dataTextView)).check(matches(withText(testBarcode.rawBarcode.value)))
        onView(withId(R.id.titleEditText)).check(matches(withText(testBarcode.title)))
        onView(withId(R.id.colorFrameView)).check(matches(withColor(testBarcode.color ?: Color.TRANSPARENT)))
    }

    @Test
    fun checkCorrectBarcodeInformationWhenSave() {
        launchAsSave()
        onView(withId(R.id.dataTextView)).check(matches(withText(testBarcodeTemplate.rawBarcode.value)))
    }
}
