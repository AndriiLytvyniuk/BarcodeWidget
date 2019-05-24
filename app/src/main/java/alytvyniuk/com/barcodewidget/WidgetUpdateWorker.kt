package alytvyniuk.com.barcodewidget

import alytvyniuk.com.barcodewidget.converters.CodeToImageConverter
import alytvyniuk.com.barcodewidget.db.BarcodeDao
import alytvyniuk.com.barcodewidget.model.Barcode
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.Rect
import android.util.Log
import android.widget.RemoteViews
import androidx.work.ListenableWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.Worker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

private const val TAG = "WidgetUpdateWorker"

private const val FRAME_WIDTH = 7
private const val EXTRA_WIDGET_IDS = "EXTRA_WIDGET_IDS"
private const val EXTRA_ACTION = "EXTRA_ACTION"

class WidgetUpdateWorker(private val appContext: Context,
                         private val workerParams: WorkerParameters,
                         private val barcodeDao: BarcodeDao,
                         private val codeToImageConverter: CodeToImageConverter)
    : Worker(appContext, workerParams)  {

    companion object {

        fun enqueueUpdate(widgetIds : IntArray) {
            val data = workDataOf(EXTRA_WIDGET_IDS to widgetIds,
                EXTRA_ACTION to AppWidgetManager.ACTION_APPWIDGET_UPDATE)
            val uploadWorkRequest = OneTimeWorkRequestBuilder<WidgetUpdateWorker>()
                .setInputData(data)
                .build()
            App.component().getWorkManager().enqueue(uploadWorkRequest)
        }

        fun enqueueDelete(widgetIds : IntArray) {
            val data = workDataOf(EXTRA_WIDGET_IDS to widgetIds,
                EXTRA_ACTION to AppWidgetManager.ACTION_APPWIDGET_DELETED)
            val uploadWorkRequest = OneTimeWorkRequestBuilder<WidgetUpdateWorker>()
                .setInputData(data)
                .build()
            App.component().getWorkManager().enqueue(uploadWorkRequest)
        }
    }

    override fun doWork(): Result {
        val widgetIds = workerParams.inputData.getIntArray(EXTRA_WIDGET_IDS) ?: return Result.failure()
        val action = workerParams.inputData.getString(EXTRA_ACTION) ?: return Result.failure()
        when (action) {
            AppWidgetManager.ACTION_APPWIDGET_UPDATE -> {
                runBlocking {
                    val barcodes = barcodeDao.loadBarcodeEntities(widgetIds.asList())
                    val appWidgetManager = AppWidgetManager.getInstance(appContext)
                    for (barcode in barcodes) {
                        updateWidget(appContext, appWidgetManager, barcode)
                    }
                }
            }
            AppWidgetManager.ACTION_APPWIDGET_DELETED -> {
                runBlocking {
                    barcodeDao.eraseWidgetIds(widgetIds.asList())
                }
            }
        }
        return Result.success()
    }

    private fun updateWidget(context: Context, appWidgetManager: AppWidgetManager, barcode: Barcode) {
        val remoteViews = RemoteViews(context.packageName, R.layout.widget_layout)
        Log.d(TAG, "Update widget for id ${barcode.widgetId}, barcode = $barcode")
        val bitmap = runBlocking {
            codeToImageConverter.convert(barcode.rawBarcode)
        }
        remoteViews.setOnClickPendingIntent(R.id.widgetImageView, getOnClickIntent(context, barcode))
        val bitmapWithFrame = createFrameBitmap(context, bitmap, barcode.color ?: Color.TRANSPARENT)
        remoteViews.setBitmap(R.id.widgetImageView, "setImageBitmap", bitmapWithFrame)
        appWidgetManager.updateAppWidget(barcode.widgetId, remoteViews)
    }

    /**
     * This is the only way to create stable frame around ImageView with same ratio
     */
    private fun createFrameBitmap(context: Context, imageBitmap: Bitmap, color: Int): Bitmap {
        val bitmap = Bitmap.createBitmap(imageBitmap.width, imageBitmap.height, Bitmap.Config.ARGB_8888)
        val drawable = context.getDrawable(R.drawable.widget_frame)
        drawable.colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, imageBitmap.width, imageBitmap.height)
        drawable.draw(canvas)
        canvas.drawBitmap(imageBitmap, null,
            Rect(FRAME_WIDTH, FRAME_WIDTH, bitmap.width - FRAME_WIDTH, bitmap.height - FRAME_WIDTH), null)
        return bitmap
    }

    private fun getOnClickIntent(context: Context, barcode: Barcode): PendingIntent {
        val intent = PreviewActivity.intent(context, barcode)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        return PendingIntent.getActivity(context, barcode.widgetId, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }
}

class WidgetUpdateWorkerFactory(val barcodeDao: BarcodeDao,
                                val codeToImageConverter: CodeToImageConverter) : WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        return WidgetUpdateWorker(appContext, workerParameters, barcodeDao, codeToImageConverter)
    }
}