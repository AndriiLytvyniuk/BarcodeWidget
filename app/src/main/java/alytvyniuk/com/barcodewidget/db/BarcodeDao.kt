package alytvyniuk.com.barcodewidget.db

import alytvyniuk.com.barcodewidget.model.Barcode
import javax.inject.Inject
import javax.inject.Singleton

interface BarcodeDao {
    companion object {
        const val INVALID_DB_ID = 0
    }

    suspend fun insert(barcode: Barcode)
    suspend fun loadBarcodeEntities(widgetIds: List<Int>): List<Barcode>
    suspend fun loadAll(): List<Barcode>
    suspend fun update(barcode: Barcode)
    suspend fun eraseWidgetIds(widgetIds: List<Int>): Int
    suspend fun delete(barcode: Barcode): Int
}

@Singleton
class RoomBarcodeDaoImpl @Inject constructor(private val roomBarcodeDao: RoomBarcodeDao) : BarcodeDao {

    override suspend fun insert(barcode: Barcode) {
        val roomEntity = RoomBarcode(barcode)
        roomBarcodeDao.insert(roomEntity)
    }

    override suspend fun loadBarcodeEntities(widgetIds: List<Int>) : List<Barcode> {
        val roomEntities = roomBarcodeDao.loadBarcodeEntities(widgetIds)
        return roomEntities.map { it.toBarcodeEntity() }
    }

    override suspend fun loadAll(): List<Barcode> {
        val entities = roomBarcodeDao.loadAll()
        return entities.map { it.toBarcodeEntity() }
    }

    override suspend fun update(barcode: Barcode) {
        val roomEntity = RoomBarcode(barcode)
        roomBarcodeDao.update(roomEntity)
    }

    override suspend fun eraseWidgetIds(widgetIds: List<Int>) = roomBarcodeDao.eraseWidgetIds(widgetIds)

    override suspend fun delete(barcode: Barcode) = roomBarcodeDao.delete(RoomBarcode(barcode))
}
