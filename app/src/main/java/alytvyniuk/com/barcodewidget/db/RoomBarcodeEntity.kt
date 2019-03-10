package alytvyniuk.com.barcodewidget.db

import alytvyniuk.com.barcodewidget.model.Barcode
import alytvyniuk.com.barcodewidget.model.BarcodeEntity
import alytvyniuk.com.barcodewidget.model.Format
import android.appwidget.AppWidgetManager
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RoomBarcodeEntity (@ColumnInfo val widgetId : Int = AppWidgetManager.INVALID_APPWIDGET_ID,
                              @ColumnInfo val barcodeFormat : String,
                              @ColumnInfo val data : String,
                              @PrimaryKey(autoGenerate = true) var id: Int = 0) {


    constructor(entity: BarcodeEntity) : this(entity.widgetId,
        entity.barcode.format.toString(), entity.barcode.value, entity.id)

    fun toBarcodeEntity() : BarcodeEntity {
        val barcodeFormat = Format.valueOf(barcodeFormat)
        val barcode = Barcode(barcodeFormat, data)
        return BarcodeEntity(barcode, widgetId, id)
    }
}
