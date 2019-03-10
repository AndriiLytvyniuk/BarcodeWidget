package alytvyniuk.com.barcodewidget.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

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
}