package alytvyniuk.com.barcodewidget.db

import alytvyniuk.com.barcodewidget.model.Barcode
import javax.inject.Inject
import javax.inject.Singleton

interface BarcodeDao {
    companion object {
        const val INVALID_DB_ID = 0
    }
    fun insert(barcode: Barcode)
    fun loadBarcodeEntity(widgetId: Int) : Barcode?
    fun loadAll() : List<Barcode>
    fun update(barcode: Barcode)
    fun eraseWidgetId(widgetId: Int) : Int
    fun delete(barcode: Barcode) : Int
}

@Singleton
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

    override fun eraseWidgetId(widgetId: Int) = roomBarcodeDao.eraseWidgetId(widgetId)

    override fun delete(barcode: Barcode) : Int {
        val roomEntity = RoomBarcode(barcode)
        return roomBarcodeDao.delete(roomEntity)
    }
}