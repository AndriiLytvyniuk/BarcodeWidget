package alytvyniuk.com.barcodewidget.db

import alytvyniuk.com.barcodewidget.model.Barcode
import alytvyniuk.com.barcodewidget.model.BarcodeEntity
import alytvyniuk.com.barcodewidget.model.Format
import android.appwidget.AppWidgetManager
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

fun Barcode.toBarcodeEntity(widgetId: Int) : RoomBarcodeEntity {
    return RoomBarcodeEntity(widgetId, format.toString(), value)
}

@Entity
data class RoomBarcodeEntity (@ColumnInfo val widgetId : Int = AppWidgetManager.INVALID_APPWIDGET_ID,
                              @ColumnInfo val barcodeFormat : String,
                              @ColumnInfo val data : String) {
    @PrimaryKey(autoGenerate = true) var id: Int = 0

    fun toBarcodeEntity() : BarcodeEntity {
        val barcodeFormat = Format.valueOf(barcodeFormat)
        val barcode = Barcode(barcodeFormat, data)
        return BarcodeEntity(barcode, widgetId)
    }
}
