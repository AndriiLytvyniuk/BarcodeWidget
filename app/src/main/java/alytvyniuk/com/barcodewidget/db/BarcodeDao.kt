package alytvyniuk.com.barcodewidget.db

import alytvyniuk.com.barcodewidget.model.Barcode
import javax.inject.Inject

interface BarcodeDao {
    fun insert(barcode: Barcode)
    fun loadBarcodeEntity(widgetId: Int) : Barcode?
    fun loadAll() : List<Barcode>
    fun update(barcode: Barcode)
}

class RoomBarcodeDaoImpl @Inject constructor(private val roomBarcodeDao: RoomBarcodeDao) : BarcodeDao {
    override fun insert(barcode: Barcode) {
        val roomEntity = RoomBarcode(barcode)
        roomBarcodeDao.insert(roomEntity)
    }

    override fun loadBarcodeEntity(widgetId: Int) : Barcode? {
        val roomEntity = roomBarcodeDao.loadBarcodeEntity(widgetId)
        return roomEntity?.toBarcodeEntity()
    }

    override fun loadAll() : List<Barcode> {
        val entities = roomBarcodeDao.loadAll()
        return entities.map { it.toBarcodeEntity() }
    }

    override fun update(barcode: Barcode) {
        val roomEntity = RoomBarcode(barcode)
        roomBarcodeDao.update(roomEntity)
    }
}

//class StubDao : BarcodeDao {
//    override fun insert(rawBarcode: RawBarcode, widgetId: Int) {
//    }
//
//    override fun loadBarcodeEntity(widgetId: Int): Barcode? {
//        return Barcode(RawBarcode(Format.QR_CODE, "StubDaoBarcode"), 1)
//    }
//
//    override fun loadAll(): List<Barcode> {
//        return listOf(Barcode(RawBarcode(Format.QR_CODE, "StubDaoBarcode1"), 1),
//            Barcode(RawBarcode(Format.AZTEC, "StubDaoBarcode2"), 2),
//            Barcode(RawBarcode(Format.CODABAR, "StubDaoBarcode3"), 3))
//    }
//
//}