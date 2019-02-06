package alytvyniuk.com.barcodewidget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.app.PendingIntent


class BarcodeWidgetProvider : AppWidgetProvider() {

    companion object {
        private const val WIDGET_REQUEST_CODE = 2
    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        for (widgetId in appWidgetIds) {

            val remoteViews = RemoteViews(context.packageName, R.layout.widget_layout)
            remoteViews.setOnClickPendingIntent(R.id.widget_root_view, getOnClickIntent(context))
            appWidgetManager.updateAppWidget(widgetId, remoteViews)
        }
    }

    private fun getOnClickIntent(context: Context) : PendingIntent {
        val intent = Intent(context, BarcodePreviewActivity::class.java)
        return PendingIntent.getActivity(context, WIDGET_REQUEST_CODE, intent, 0)
    }
}