package alytvyniuk.com.barcodewidget.db

import alytvyniuk.com.barcodewidget.model.Barcode
import alytvyniuk.com.barcodewidget.model.Format
import javax.inject.Inject

interface BarcodeDao {
    fun insert(barcode: Barcode, widgetId: Int?)
    fun loadBarcodeEntity(widgetId: Int) : Barcode?
    fun loadAll() : List<Barcode>
}

class RoomBarcodeDaoImpl @Inject constructor(private val roomBarcodeDao: RoomBarcodeDao) : BarcodeDao {

    override fun insert(barcode: Barcode, widgetId: Int?) {
        val barcodeEntity = barcode.toBarcodeEntity(widgetId)
        roomBarcodeDao.insert(barcodeEntity)
    }

    override fun loadBarcodeEntity(widgetId: Int) : Barcode? {
        val barcodeEntity = roomBarcodeDao.loadBarcodeEntity(widgetId)
        return barcodeEntity?.toBarcode()
    }

    override fun loadAll() : List<Barcode> {
        val entities = roomBarcodeDao.loadAll()
        return entities.map { it.toBarcode() }
    }
}

class StubDao : BarcodeDao {
    override fun insert(barcode: Barcode, widgetId: Int?) {
    }

    override fun loadBarcodeEntity(widgetId: Int): Barcode? {
        return Barcode(Format.QR_CODE, "StubDaoBarcode")
    }

    override fun loadAll(): List<Barcode> {
        return listOf(Barcode(Format.QR_CODE, "StubDaoBarcode1"),
            Barcode(Format.AZTEC, "StubDaoBarcode2"),
            Barcode(Format.CODABAR, "StubDaoBarcode3"))
    }

}