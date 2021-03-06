package alytvyniuk.com.barcodewidget.db

import alytvyniuk.com.barcodewidget.model.Barcode
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton

interface BarcodeDao {
    companion object {
        const val INVALID_DB_ID = 0
    }

    fun insert(barcode: Barcode): Observable<Unit>
    fun loadBarcodeEntities(widgetIds: List<Int>): Observable<List<Barcode>>
    fun loadAll(): Observable<List<Barcode>>
    fun update(barcode: Barcode): Observable<Unit>
    fun eraseWidgetIds(widgetIds: List<Int>): Observable<Int>
    fun delete(barcode: Barcode): Observable<Int>
}

@Singleton
class RoomBarcodeDaoImpl @Inject constructor(private val roomBarcodeDao: RoomBarcodeDao) : BarcodeDao {

    override fun insert(barcode: Barcode): Observable<Unit> = Observable.fromCallable {
        val roomEntity = RoomBarcode(barcode)
        roomBarcodeDao.insert(roomEntity)
    }

    override fun loadBarcodeEntities(widgetIds: List<Int>): Observable<List<Barcode>> = Observable.fromCallable {
        val roomEntities = roomBarcodeDao.loadBarcodeEntities(widgetIds)
        roomEntities.map { it.toBarcodeEntity() }
    }

    override fun loadAll(): Observable<List<Barcode>> = Observable.fromCallable {
        val entities = roomBarcodeDao.loadAll()
        entities.map { it.toBarcodeEntity() }
    }

    override fun update(barcode: Barcode): Observable<Unit> = Observable.fromCallable {
        val roomEntity = RoomBarcode(barcode)
        roomBarcodeDao.update(roomEntity)
    }

    override fun eraseWidgetIds(widgetIds: List<Int>): Observable<Int> = Observable.fromCallable {
        roomBarcodeDao.eraseWidgetIds(widgetIds)
    }

    override fun delete(barcode: Barcode): Observable<Int> = Observable.fromCallable {
        val roomEntity = RoomBarcode(barcode)
        roomBarcodeDao.delete(roomEntity)
    }
}
