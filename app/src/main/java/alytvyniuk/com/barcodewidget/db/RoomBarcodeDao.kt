package alytvyniuk.com.barcodewidget.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RoomBarcodeDao {

    @Insert
    fun insert(barcode: BarcodeEntity)

    @Query("SELECT * FROM BarcodeEntity WHERE widgetId = :widgetId")
    fun loadBarcodeEntity(widgetId: Int) : BarcodeEntity?

    @Query("SELECT * FROM BarcodeEntity")
    fun loadAll() : List<BarcodeEntity>
}