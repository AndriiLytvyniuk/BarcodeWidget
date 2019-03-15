package alytvyniuk.com.barcodewidget

import alytvyniuk.com.barcodewidget.converters.CodeToImageConverter
import alytvyniuk.com.barcodewidget.db.BarcodeDao
import alytvyniuk.com.barcodewidget.model.Barcode
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.widget.RemoteViews
import javax.inject.Inject

private const val TAG = "BarcodeWidgetProvider"
private const val WIDGET_REQUEST_CODE = 2

class BarcodeWidgetProvider : AppWidgetProvider() {

    @Inject lateinit var codeToImageConverter: CodeToImageConverter
    @Inject lateinit var barcodeDao: BarcodeDao

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
        App.component().inject(this)
        for (widgetId in appWidgetIds) {
            updateWidget(context, appWidgetManager, widgetId)
        }
    }

    private fun updateWidget(context: Context, appWidgetManager: AppWidgetManager, widgetId : Int) {
        val remoteViews = RemoteViews(context.packageName, R.layout.widget_layout)
        val barcode = barcodeDao.loadBarcodeEntity(widgetId)
        Log.d(TAG, "Update widget for id $widgetId, rawBarcode = $barcode")
        if (barcode != null) {
            val bitmap = codeToImageConverter.convert(barcode.rawBarcode)
            remoteViews.setBitmap(R.id.widgetImageView, "setImageBitmap", bitmap)
            remoteViews.setInt(R.id.imageFrame, "setBackgroundColor", barcode.color ?: Color.TRANSPARENT)
            remoteViews.setOnClickPendingIntent(R.id.widgetImageView, getOnClickIntent(context, barcode))
            appWidgetManager.updateAppWidget(widgetId, remoteViews)
        }
    }

    private fun getOnClickIntent(context: Context, barcode: Barcode) : PendingIntent {
        val intent = PreviewActivity.intent(context, barcode)
        return PendingIntent.getActivity(context, WIDGET_REQUEST_CODE, intent, 0)
    }
}