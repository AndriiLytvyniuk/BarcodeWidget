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
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.TaskStackBuilder
import javax.inject.Inject


private const val TAG = "BarcodeWidgetProvider"
private const val WIDGET_REQUEST_CODE = 2

open class BarcodeWidgetProvider : AppWidgetProvider() {

    @Inject lateinit var codeToImageConverter: CodeToImageConverter
    @Inject lateinit var barcodeDao: BarcodeDao

    override fun onReceive(context: Context, intent: Intent) {
        App.component().inject(this)
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
        for (widgetId in appWidgetIds) {
            updateWidget(context, appWidgetManager, widgetId)
        }
    }

    private fun updateWidget(context: Context, appWidgetManager: AppWidgetManager, widgetId : Int) {
        val remoteViews = RemoteViews(context.packageName, R.layout.widget_layout)
        val barcode = barcodeDao.loadBarcodeEntity(widgetId)
        Log.d(TAG, "Update widget for id $widgetId, rawBarcode = $barcode")
        barcode?.let {
            val bitmap = codeToImageConverter.convert(barcode.rawBarcode)
            remoteViews.setBitmap(R.id.widgetImageView, "setImageBitmap", bitmap)
            remoteViews.setOnClickPendingIntent(R.id.widgetImageView, getOnClickIntent(context, barcode))
            val frameBitmap = createFrameBitmap(bitmap.width, bitmap.height, barcode.color ?: Color.TRANSPARENT)
            remoteViews.setBitmap(R.id.frameImageView, "setImageBitmap", frameBitmap)
            appWidgetManager.updateAppWidget(widgetId, remoteViews)
        }
    }

    /**
     * This is the only way to create stable frame around ImageView with same ratio
     */
    private fun createFrameBitmap(width: Int, height: Int, color: Int): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val paint = Paint()
        paint.color = color
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
        return bitmap
    }

    private fun getOnClickIntent(context: Context, barcode: Barcode) : PendingIntent {
        val stackBuilder = TaskStackBuilder.create(context)
        val intent = PreviewActivity.intent(context, barcode)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        stackBuilder.addNextIntent(intent)
        return stackBuilder.getPendingIntent(WIDGET_REQUEST_CODE, PendingIntent.FLAG_UPDATE_CURRENT)!!
    }

    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        super.onDeleted(context, appWidgetIds)
        Log.d(TAG, "onDeleted: ${appWidgetIds.contentToString()}")
        for (widgetId in appWidgetIds) {
            val res = barcodeDao.eraseWidgetId(widgetId)
            if (res != 1) {
                Log.e(TAG, "Incorrect widget delete. Deleted: $res")
            }
        }
    }
}