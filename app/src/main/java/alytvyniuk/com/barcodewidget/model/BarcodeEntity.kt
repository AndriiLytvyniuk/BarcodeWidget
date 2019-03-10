package alytvyniuk.com.barcodewidget.model

import android.appwidget.AppWidgetManager

data class BarcodeEntity(
    var barcode: Barcode,
    var widgetId: Int = AppWidgetManager.INVALID_APPWIDGET_ID,
    var id: Int = INVALID_DB_ID) {

    companion object {
        const val INVALID_DB_ID = -1
    }
}

fun Int.isValidWidgetId() = this != AppWidgetManager.INVALID_APPWIDGET_ID