package alytvyniuk.com.barcodewidget.db

import alytvyniuk.com.barcodewidget.model.Barcode
import android.appwidget.AppWidgetManager
import androidx.room.*

@Dao
interface RoomBarcodeDao {

    @Insert
    fun insert(barcode: RoomBarcode)

    @Query("SELECT * FROM RoomBarcode WHERE widgetId = :widgetId")
    fun loadBarcodeEntity(widgetId: Int) : RoomBarcode?

    @Query("SELECT * FROM RoomBarcode")
    fun loadAll() : List<RoomBarcode>

    @Update
    fun update(barcode: RoomBarcode)

    @Query("UPDATE RoomBarcode SET widgetId = ${AppWidgetManager.INVALID_APPWIDGET_ID} WHERE widgetId = :widgetId")
    fun eraseWidgetId(widgetId: Int) : Int

    @Delete
    fun delete(barcode: RoomBarcode): Int
}