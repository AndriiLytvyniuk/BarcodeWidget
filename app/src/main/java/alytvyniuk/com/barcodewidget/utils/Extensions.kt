package alytvyniuk.com.barcodewidget.utils

import alytvyniuk.com.barcodewidget.converters.CodeToImageConverter
import alytvyniuk.com.barcodewidget.model.Barcode
import alytvyniuk.com.barcodewidget.model.RawBarcode
import alytvyniuk.com.barcodewidget.model.isValidWidgetId
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.ImageView
import androidx.annotation.VisibleForTesting

private const val KEY_WIDGET_ID = "KEY_WIDGET_ID"
const val KEY_BARCODE = "KEY_BARCODE"

fun <T> Context.getBarcodeActivityIntent(
    clazz: Class<T>, barcode: Barcode? = null,
    widgetId: Int = AppWidgetManager.INVALID_APPWIDGET_ID) : Intent {
    val intent = Intent(this, clazz)
    return appendExtras(intent, barcode, widgetId)
}

@VisibleForTesting
fun appendExtras(
    intent: Intent, barcode: Barcode? = null,
    widgetId: Int = AppWidgetManager.INVALID_APPWIDGET_ID
): Intent {
    if (widgetId.isValidWidgetId()) {
        intent.putExtra(KEY_WIDGET_ID, widgetId)
    }
    if (barcode != null) {
        intent.putExtra(KEY_BARCODE, barcode)
    }
    return intent
}

fun Intent.getWidgetId() = getIntExtra(KEY_WIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID)

fun Intent.getBarcode(): Barcode = getParcelableExtra(KEY_BARCODE)

fun ImageView.setImageFromBarcode(
    codeToImageConverter: CodeToImageConverter,
    rawBarcode: RawBarcode,
    asyncTaskCoroutineScope: AsyncTaskCoroutineScope
) {
    visibility = View.INVISIBLE
    asyncTaskCoroutineScope.launchWithResult(
        { codeToImageConverter.convert(rawBarcode) },
        { bitmap ->
            setImageBitmap(bitmap)
            visibility = View.VISIBLE
        })
}
