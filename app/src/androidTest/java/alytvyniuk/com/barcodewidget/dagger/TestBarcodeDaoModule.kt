package alytvyniuk.com.barcodewidget.dagger

import alytvyniuk.com.barcodewidget.db.BarcodeDao
import alytvyniuk.com.barcodewidget.model.Barcode
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class TestBarcodeDaoModule {

    @Provides
    @Singleton
    fun providesBarcodeDaoMock(): BarcodeDao {
        return BarcodeDaoMock()
    }
}

class BarcodeDaoMock : BarcodeDao {

    companion object {
        private val barcodes = mutableListOf<Barcode>()

        fun setTestBarcodes(barcodes: List<Barcode>) {
            this.barcodes.clear()
            this.barcodes.addAll(barcodes)
        }
    }


    override suspend fun insert(barcode: Barcode) {
        barcodes.add(barcode)
    }

    override suspend fun loadBarcodeEntities(widgetIds: List<Int>) =
        barcodes.subList(0, widgetIds.size)


    override suspend fun loadAll(): List<Barcode> = barcodes

    override suspend fun update(barcode: Barcode) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun eraseWidgetIds(widgetIds: List<Int>): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun delete(barcode: Barcode): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

