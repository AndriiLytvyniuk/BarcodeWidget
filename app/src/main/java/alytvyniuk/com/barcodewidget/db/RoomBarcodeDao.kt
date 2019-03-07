package alytvyniuk.com.barcodewidget.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RoomBarcodeDao {

    @Insert
    fun insert(barcode: RoomBarcodeEntity)

    @Query("SELECT * FROM RoomBarcodeEntity WHERE widgetId = :widgetId")
    fun loadBarcodeEntity(widgetId: Int) : RoomBarcodeEntity?

    @Query("SELECT * FROM RoomBarcodeEntity")
    fun loadAll() : List<RoomBarcodeEntity>
}