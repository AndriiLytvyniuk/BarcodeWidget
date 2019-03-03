package alytvyniuk.com.barcodewidget.dagger

import alytvyniuk.com.barcodewidget.db.BarcodeDB
import alytvyniuk.com.barcodewidget.db.RoomBarcodeDao
import android.content.Context
import androidx.annotation.NonNull
import androidx.room.Room
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

private const val DB_NAME = "barcode_db"

@Module(includes = [AppContextModule::class])
class RoomBarcodeDaoModule {

    @Provides
    @Singleton
    @NonNull
    fun providesRoomBarcodeDao(context: Context) : RoomBarcodeDao {
        return Room.databaseBuilder(context, BarcodeDB::class.java, DB_NAME)
            .allowMainThreadQueries()
            .build()
            .getDao()
    }
}
