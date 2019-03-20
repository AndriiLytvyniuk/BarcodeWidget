package alytvyniuk.com.barcodewidget.dagger

import alytvyniuk.com.barcodewidget.db.BarcodeDao
import alytvyniuk.com.barcodewidget.model.Barcode
import alytvyniuk.com.barcodewidget.model.Format
import alytvyniuk.com.barcodewidget.model.RawBarcode
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


    override fun insert(barcode: Barcode) {
        barcodes.add(barcode)
    }

    override fun loadBarcodeEntity(widgetId: Int): Barcode? {
        return if (barcodes.isEmpty()) null else barcodes[0]
    }

    override fun loadAll(): List<Barcode> {
        return barcodes
    }

    override fun update(barcode: Barcode) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun eraseWidgetId(widgetId: Int): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun delete(barcode: Barcode): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

