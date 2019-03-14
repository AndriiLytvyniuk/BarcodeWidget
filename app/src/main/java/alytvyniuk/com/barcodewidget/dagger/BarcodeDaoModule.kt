package alytvyniuk.com.barcodewidget.dagger;

import alytvyniuk.com.barcodewidget.db.BarcodeDao
import alytvyniuk.com.barcodewidget.db.RoomBarcodeDaoImpl
import dagger.Binds
import dagger.Module

@Module(includes = [RoomBarcodeDaoModule::class])
abstract class BarcodeDaoModule {

    @Binds
    abstract fun providesBarcodeDao(roomBarcodeDaoImpl: RoomBarcodeDaoImpl) : BarcodeDao
}
