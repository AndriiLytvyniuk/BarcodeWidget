package alytvyniuk.com.barcodewidget

import alytvyniuk.com.barcodewidget.converters.CodeToImageConverter
import alytvyniuk.com.barcodewidget.db.BarcodeDao
import alytvyniuk.com.barcodewidget.model.Barcode
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.JobIntentService
import javax.inject.Inject

private const val TAG = "WidgetUpdateService"

private const val JOB_ID = 1000
private const val EXTRA_WIDGET_IDS = "EXTRA_WIDGET_IDS"

class WidgetUpdateService : JobIntentService() {

    @Inject
    lateinit var codeToImageConverter: CodeToImageConverter
    @Inject
    lateinit var barcodeDao: BarcodeDao

    companion object {
        fun enqueueUpdate(context: Context, widgetIds : ArrayList<Int>) {
            enqueueWork(context, Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE)
                .putIntegerArrayListExtra(EXTRA_WIDGET_IDS, widgetIds))
        }

        fun enqueueDelete(context: Context, widgetIds : ArrayList<Int>) {
            enqueueWork(context, Intent(AppWidgetManager.ACTION_APPWIDGET_DELETED)
                .putIntegerArrayListExtra(EXTRA_WIDGET_IDS, widgetIds))
        }

        private fun enqueueWork(context: Context, work: Intent) {
            enqueueWork(context, WidgetUpdateService::class.java, JOB_ID, work)
        }
    }

    override fun onCreate() {
        super.onCreate()
        App.component().inject(this)
    }

    override fun onHandleWork(intent: Intent) {
        val widgetIds = intent.getIntegerArrayListExtra(EXTRA_WIDGET_IDS) ?: return
        when (intent.action) {
            AppWidgetManager.ACTION_APPWIDGET_UPDATE -> {
                val barcodes = barcodeDao.loadBarcodeEntities(widgetIds).blockingFirst()
                val appWidgetManager = AppWidgetManager.getInstance(this)
                for (barcode in barcodes) {
                    updateWidget(this, appWidgetManager, barcode)
                }
            }
            AppWidgetManager.ACTION_APPWIDGET_DELETED -> {
                barcodeDao.eraseWidgetIds(widgetIds).blockingFirst()
            }
        }
    }

    private fun updateWidget(context: Context, appWidgetManager: AppWidgetManager, barcode: Barcode) {
        val remoteViews = RemoteViews(context.packageName, R.layout.widget_layout)
        Log.d(TAG, "Update widget for id ${barcode.widgetId}, barcode = $barcode")
        val bitmap = codeToImageConverter.convert(barcode.rawBarcode).blockingFirst()
        remoteViews.setOnClickPendingIntent(R.id.widgetImageView, getOnClickIntent(context, barcode))
        val bitmapWithFrame = createFrameBitmap(bitmap, barcode.color ?: Color.TRANSPARENT)
        remoteViews.setBitmap(R.id.widgetImageView, "setImageBitmap", bitmapWithFrame)
        appWidgetManager.updateAppWidget(barcode.widgetId, remoteViews)
    }

    /**
     * This is the only way to create stable frame around ImageView with same ratio
     */
    private fun createFrameBitmap(imageBitmap: Bitmap, color: Int): Bitmap {
        val bitmap = Bitmap.createBitmap(imageBitmap.width, imageBitmap.height, Bitmap.Config.ARGB_8888)
        val drawable = getDrawable(R.drawable.widget_frame)
        drawable.colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, imageBitmap.width, imageBitmap.height)
        drawable.draw(canvas)
        val frameWidth = 8
        canvas.drawBitmap(imageBitmap, null,
            Rect(frameWidth, frameWidth, bitmap.width - frameWidth, bitmap.height - frameWidth), null)
        return bitmap
    }

    private fun getOnClickIntent(context: Context, barcode: Barcode): PendingIntent {
        val intent = PreviewActivity.intent(context, barcode)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        return PendingIntent.getActivity(context, barcode.widgetId, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }
}