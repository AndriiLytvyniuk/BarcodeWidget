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
    fun providesBarcodeDaoMock() : BarcodeDao {
        return object : BarcodeDao {
            override fun insert(barcode: Barcode) {

            }

            override fun loadBarcodeEntity(widgetId: Int): Barcode? {
                return Barcode(RawBarcode(Format.QR_CODE, "first"))
            }

            override fun loadAll(): List<Barcode> {
                return listOf(Barcode(RawBarcode(Format.QR_CODE, "first")),
                    Barcode(RawBarcode(Format.CODABAR, "second")),
                    Barcode(RawBarcode(Format.DATA_MATRIX, "third")))
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
    }
}