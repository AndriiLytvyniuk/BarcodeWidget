package alytvyniuk.com.barcodewidget

import alytvyniuk.com.barcodewidget.converters.CodeToImageConverter
import alytvyniuk.com.barcodewidget.db.BarcodeDao
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import javax.inject.Inject

private const val TAG = "BarcodeWidgetProvider"
private const val WIDGET_REQUEST_CODE = 2

class BarcodeWidgetProvider : AppWidgetProvider() {

    @Inject lateinit var codeToImageConverter: CodeToImageConverter
    @Inject lateinit var barcodeDao: BarcodeDao

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        Log.d(TAG, "onUpdate ${appWidgetIds.size}")
        App.component().inject(this)
        for (widgetId in appWidgetIds) {
            updateWidget(context, appWidgetManager, widgetId)
        }
    }

    private fun updateWidget(context: Context, appWidgetManager: AppWidgetManager, widgetId : Int) {
        val remoteViews = RemoteViews(context.packageName, R.layout.widget_layout)
        remoteViews.setOnClickPendingIntent(R.id.widget_image, getOnClickIntent(context))
        val barcode = barcodeDao.loadBarcodeEntity(widgetId)
        Log.d(TAG, "Update widget for id $widgetId, barcode = $barcode")
        if (barcode != null) {
            val bitmap = codeToImageConverter.convert(barcode)
            remoteViews.setBitmap(R.id.widget_image, "setImageBitmap", bitmap)
            appWidgetManager.updateAppWidget(widgetId, remoteViews)
        }
    }

    private fun getOnClickIntent(context: Context) : PendingIntent {
        val intent = Intent(context, PreviewActivity::class.java)
        return PendingIntent.getActivity(context, WIDGET_REQUEST_CODE, intent, 0)
    }
}