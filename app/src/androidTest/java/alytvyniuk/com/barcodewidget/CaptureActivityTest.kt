package alytvyniuk.com.barcodewidget

import android.appwidget.AppWidgetManager
import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import org.hamcrest.CoreMatchers.not
import org.junit.Assert.assertEquals
import org.junit.Test

private const val WIDGET_ID = 1
class CaptureActivityTest {

    private fun launchFromHome() =
        ActivityScenario.launch<CaptureActivity>(CaptureActivity::class.java)

    private fun launchFromWidget() : ActivityScenario<CaptureActivity> {
        val intent = Intent(ApplicationProvider.getApplicationContext(), CaptureActivity::class.java)
            .putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, WIDGET_ID)
        return ActivityScenario.launch<CaptureActivity>(intent)
    }

    @Test
    fun checkButtonsVisibility() {
        launchFromHome()
        onView(withId(R.id.savedButton)).check(matches(isCompletelyDisplayed()))
        onView(withId(R.id.galleryButton)).check(matches(isCompletelyDisplayed()))
    }

    @Test
    fun checkWidgetIdFieldFromWidget() {
        launchFromWidget().onActivity { activity ->
            assertEquals(activity.newWidgetId, WIDGET_ID)
        }
    }

    @Test
    fun checkWidgetIdFieldFromHome() {
        launchFromHome().onActivity { activity ->
            assertEquals(activity.newWidgetId, AppWidgetManager.INVALID_APPWIDGET_ID)
        }
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

    @Test
    fun getWidgetIdFromIntentTestWithoutId() {
        launchFromHome().onActivity { activity ->
            val idFromIntent = activity.getWidgetIdFromIntent(Intent())
            assertEquals(AppWidgetManager.INVALID_APPWIDGET_ID, idFromIntent)
        }
    }

    @Test
    fun getWidgetIdFromIntentTestWith() {
        launchFromHome().onActivity { activity ->
            val testActivity = CaptureActivity()
            val widgetId = 5
            val intent = Intent().putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
            val idFromIntent = testActivity.getWidgetIdFromIntent(intent)
            assertEquals(widgetId, idFromIntent)
        }
    }
}