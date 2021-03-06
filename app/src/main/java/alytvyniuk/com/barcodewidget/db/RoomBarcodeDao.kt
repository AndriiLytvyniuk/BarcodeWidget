package alytvyniuk.com.barcodewidget.db

import android.appwidget.AppWidgetManager
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface RoomBarcodeDao {

    @Insert
    fun insert(barcode: RoomBarcode)

    @Query("SELECT * FROM RoomBarcode WHERE widgetId IN (:widgetIds)")
    fun loadBarcodeEntities(widgetIds: List<Int>) : List<RoomBarcode>

    @Query("SELECT * FROM RoomBarcode")
    fun loadAll() : List<RoomBarcode>

    @Update
    fun update(barcode: RoomBarcode)

    @Query("UPDATE RoomBarcode SET widgetId = ${AppWidgetManager.INVALID_APPWIDGET_ID} WHERE widgetId IN (:widgetIds)")
    fun eraseWidgetIds(widgetIds: List<Int>) : Int

    @Delete
    fun delete(barcode: RoomBarcode): Int
}
