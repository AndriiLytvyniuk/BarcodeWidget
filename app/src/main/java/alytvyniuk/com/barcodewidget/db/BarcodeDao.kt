package alytvyniuk.com.barcodewidget.db

import alytvyniuk.com.barcodewidget.model.BarcodeEntity
import javax.inject.Inject

interface BarcodeDao {
    fun insert(barcodeEntity: BarcodeEntity)
    fun loadBarcodeEntity(widgetId: Int) : BarcodeEntity?
    fun loadAll() : List<BarcodeEntity>
}

class RoomBarcodeDaoImpl @Inject constructor(private val roomBarcodeDao: RoomBarcodeDao) : BarcodeDao {

    override fun insert(barcodeEntity: BarcodeEntity) {
        val roomEntity = RoomBarcodeEntity(barcodeEntity)
        roomBarcodeDao.insert(roomEntity)
    }

    override fun loadBarcodeEntity(widgetId: Int) : BarcodeEntity? {
        val roomEntity = roomBarcodeDao.loadBarcodeEntity(widgetId)
        return roomEntity?.toBarcodeEntity()
    }

    override fun loadAll() : List<BarcodeEntity> {
        val entities = roomBarcodeDao.loadAll()
        return entities.map { it.toBarcodeEntity() }
    }
}

//class StubDao : BarcodeDao {
//    override fun insert(barcode: Barcode, widgetId: Int) {
//    }
//
//    override fun loadBarcodeEntity(widgetId: Int): BarcodeEntity? {
//        return BarcodeEntity(Barcode(Format.QR_CODE, "StubDaoBarcode"), 1)
//    }
//
//    override fun loadAll(): List<BarcodeEntity> {
//        return listOf(BarcodeEntity(Barcode(Format.QR_CODE, "StubDaoBarcode1"), 1),
//            BarcodeEntity(Barcode(Format.AZTEC, "StubDaoBarcode2"), 2),
//            BarcodeEntity(Barcode(Format.CODABAR, "StubDaoBarcode3"), 3))
//    }
//
//}