package alytvyniuk.com.barcodewidget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log


private const val TAG = "BarcodeWidgetProvider"

open class BarcodeWidgetProvider : AppWidgetProvider() {

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (Intent.ACTION_BOOT_COMPLETED == intent.action) {
            Log.d(TAG, "onReceive ${Intent.ACTION_BOOT_COMPLETED}")
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val widgetIds = appWidgetManager.getAppWidgetIds(ComponentName(context, javaClass))
            onUpdate(context, AppWidgetManager.getInstance(context), widgetIds)
        }
    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        Log.d(TAG, "onUpdate ${appWidgetIds.size}")
        WidgetUpdateService.enqueueUpdate(context, appWidgetIds.toCollection(ArrayList()))
    }

    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        super.onDeleted(context, appWidgetIds)
        Log.d(TAG, "onDeleted: ${appWidgetIds.contentToString()}")
        WidgetUpdateService.enqueueDelete(context, appWidgetIds.toCollection(ArrayList()))
    }
}
