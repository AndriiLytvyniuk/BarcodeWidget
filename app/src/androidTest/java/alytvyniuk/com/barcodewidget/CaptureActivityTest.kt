package alytvyniuk.com.barcodewidget

import android.appwidget.AppWidgetManager
import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import org.hamcrest.CoreMatchers.not
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

private const val WIDGET_ID = 1
class CaptureActivityTest {

    @get:Rule
    val rule = ActivityTestRule<CaptureActivity>(CaptureActivity::class.java, false, false)

    private fun launchFromHome() =
        rule.launchActivity(Intent())

    private fun launchFromWidget() =
        rule.launchActivity(Intent().putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, WIDGET_ID))

    @Test
    fun checkButtonsVisibility() {
        launchFromHome()
        onView(withId(R.id.savedButton)).check(matches(isCompletelyDisplayed()))
        onView(withId(R.id.galleryButton)).check(matches(isCompletelyDisplayed()))
        onView(withId(R.id.photoButton)).check(matches(isCompletelyDisplayed()))
    }

    @Test
    fun checkWidgetIdFieldFromWidget() {
        val activity = launchFromWidget()
        assertEquals(activity.newWidgetId, WIDGET_ID)
    }

    @Test
    fun checkWidgetIdFieldFromHome() {
        val activity = launchFromHome()
        assertEquals(activity.newWidgetId, AppWidgetManager.INVALID_APPWIDGET_ID)
    }

    @Test
    fun checkUpdateWidgetTextViewFromWidget() {
        launchFromWidget()
        onView(withId(R.id.updateWidgetTextView)).check(matches(isCompletelyDisplayed()))
    }

    @Test
    fun checkUpdateWidgetTextViewFromHome() {
        launchFromHome()
        onView(withId(R.id.updateWidgetTextView)).check(matches(not(isDisplayed())))
    }
}