package alytvyniuk.com.barcodewidget

import alytvyniuk.com.barcodewidget.dagger.BarcodeDaoMock
import alytvyniuk.com.barcodewidget.model.Barcode
import alytvyniuk.com.barcodewidget.model.Format
import alytvyniuk.com.barcodewidget.model.RawBarcode
import android.graphics.Color
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import org.junit.Before
import org.junit.Test
import testutils.RecyclerViewItemCountAssertion
import testutils.withRecyclerView

class ListActivityTest {

    private val testBarcodes = listOf(
        Barcode(RawBarcode(Format.QR_CODE, "first"), title = "firstTitle", color = Color.BLUE),
        Barcode(RawBarcode(Format.CODABAR, "1345"), color = Color.YELLOW),
        Barcode(RawBarcode(Format.DATA_MATRIX, "third"), title = "thirdTitle", color = Color.RED)
    )

    @Before
    fun before() {
        BarcodeDaoMock.setTestBarcodes(testBarcodes)
        ActivityScenario.launch(ListActivity::class.java)
    }

    @Test
    fun checkListViewCount() {
        onView(withId(R.id.barcodeListView)).check(RecyclerViewItemCountAssertion(testBarcodes.size + 1))
    }

    //TODO add more UI Tests
    @Test
    fun checkListViewItem() {
        onView(withRecyclerView(R.id.barcodeListView).atPositionOnView(1, R.id.dataTextView))
            .check(ViewAssertions.matches(ViewMatchers.withText(testBarcodes[0].rawBarcode.value)))
    }
}
