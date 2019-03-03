package alytvyniuk.com.barcodewidget.db

import alytvyniuk.com.barcodewidget.model.Barcode
import alytvyniuk.com.barcodewidget.model.Format
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class BarcodeEntity (@ColumnInfo val widgetId : Int,
                          @ColumnInfo val barcodeFormat : String,
                          @ColumnInfo val data : String) {

    @PrimaryKey(autoGenerate = true) val id : Int = 0

    fun toBarcode() : Barcode {
        val barcodeFormat = Format.valueOf(barcodeFormat)
        return Barcode(barcodeFormat, data)
    }

    fun Barcode.toBarcodeEntity(widgetId: Int) : BarcodeEntity {
        return BarcodeEntity(widgetId, format.toString(), value)
    }
}
