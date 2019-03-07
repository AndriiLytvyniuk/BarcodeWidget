package alytvyniuk.com.barcodewidget.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [RoomBarcodeEntity::class], version = 1)
abstract class BarcodeDB : RoomDatabase() {

    abstract fun getDao() : RoomBarcodeDao
}