package alytvyniuk.com.barcodewidget

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers.withId
import org.junit.Test
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.ViewAssertion
import org.junit.Assert.assertEquals
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import org.junit.Before
import testutils.RecyclerViewMatcher

class ListActivityTest {

    @Before
    fun before() {
        ActivityScenario.launch(ListActivity::class.java)
    }

    @Test
    fun checkListViewCount() {
        onView(withId(R.id.barcodeListView)).check(RecyclerViewItemCountAssertion(3))
    }

    @Test
    fun checkListViewItem() {
        onView(withRecyclerView(R.id.barcodeListView).atPositionOnView(0, R.id.dataTextView))
            .check(ViewAssertions.matches(ViewMatchers.withText("first")))
    }
}

fun withRecyclerView(recyclerViewId: Int): RecyclerViewMatcher {
    return RecyclerViewMatcher(recyclerViewId)
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