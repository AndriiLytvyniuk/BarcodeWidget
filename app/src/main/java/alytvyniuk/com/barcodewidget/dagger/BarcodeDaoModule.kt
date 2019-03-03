package alytvyniuk.com.barcodewidget.dagger;

import alytvyniuk.com.barcodewidget.db.BarcodeDao
import alytvyniuk.com.barcodewidget.db.RoomBarcodeDao
import androidx.annotation.NonNull
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [RoomBarcodeDaoModule::class])
class BarcodeDaoModule {

    @Provides
    @Singleton
    @NonNull
    fun providesBarcodeDao(roomBarcodeDao: RoomBarcodeDao) : BarcodeDao {
        return BarcodeDao(roomBarcodeDao)
    }
}
