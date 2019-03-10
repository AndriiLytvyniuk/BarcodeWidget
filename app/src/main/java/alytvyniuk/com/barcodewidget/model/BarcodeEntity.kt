package alytvyniuk.com.barcodewidget.model

import android.appwidget.AppWidgetManager

data class BarcodeEntity(var barcode: Barcode,
                         var widgetId : Int = AppWidgetManager.INVALID_APPWIDGET_ID)

fun Int.isValidWidgetId() = this != AppWidgetManager.INVALID_APPWIDGET_ID