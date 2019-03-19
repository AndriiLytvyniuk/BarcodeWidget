package alytvyniuk.com.barcodewidget

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.rule.ActivityTestRule
import org.junit.Rule
import org.junit.Test
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.ViewAssertion
import junit.framework.Assert.assertEquals


class ListActivityTest {

    @get:Rule
    val rule = ActivityTestRule<ListActivity>(ListActivity::class.java)

    @Test
    fun checkListViewCount() {
        onView(withId(R.id.barcodeListView)).check(RecyclerViewItemCountAssertion(3))
    }
}

class RecyclerViewItemCountAssertion(private val expectedCount: Int) : ViewAssertion {

    override fun check(view: View, noViewFoundException: NoMatchingViewException?) {
        if (noViewFoundException != null) {
            throw noViewFoundException
        }

        val recyclerView = view as RecyclerView
        val adapter = recyclerView.adapter
        assertEquals(adapter!!.itemCount, expectedCount)
    }
}