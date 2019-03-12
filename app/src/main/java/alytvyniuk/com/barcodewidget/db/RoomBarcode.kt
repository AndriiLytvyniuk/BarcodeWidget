package alytvyniuk.com.barcodewidget.db

import alytvyniuk.com.barcodewidget.model.RawBarcode
import alytvyniuk.com.barcodewidget.model.Barcode
import alytvyniuk.com.barcodewidget.model.Format
import android.appwidget.AppWidgetManager
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RoomBarcode(
    @ColumnInfo val widgetId: Int = AppWidgetManager.INVALID_APPWIDGET_ID,
    @ColumnInfo val barcodeFormat: String,
    @ColumnInfo val data: String,
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo val title: String = "",
    @ColumnInfo val color: Int? = null
) {
    constructor(barcode: Barcode) : this(
        barcode.widgetId,
        barcode.rawBarcode.format.toString(), barcode.rawBarcode.value, barcode.id, barcode.title, barcode.color
    )

    fun toBarcodeEntity(): Barcode {
        val barcodeFormat = Format.valueOf(barcodeFormat)
        val rawBarcode = RawBarcode(barcodeFormat, data)
        return Barcode(rawBarcode, widgetId, id, title, color)
    }
}
