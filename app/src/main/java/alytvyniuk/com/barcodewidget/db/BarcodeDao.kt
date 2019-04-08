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
    fun loadBarcodeEntity(widgetId: Int): Observable<Barcode?>
    fun loadAll(): Observable<List<Barcode>>
    fun update(barcode: Barcode): Observable<Unit>
    fun eraseWidgetId(widgetId: Int): Observable<Int>
    fun delete(barcode: Barcode): Observable<Int>
}

@Singleton
class RoomBarcodeDaoImpl @Inject constructor(private val roomBarcodeDao: RoomBarcodeDao) : BarcodeDao {

    override fun insert(barcode: Barcode): Observable<Unit> = Observable.fromCallable {
        val roomEntity = RoomBarcode(barcode)
        roomBarcodeDao.insert(roomEntity)
    }

    override fun loadBarcodeEntity(widgetId: Int): Observable<Barcode?> = Observable.fromCallable {
        val roomEntity = roomBarcodeDao.loadBarcodeEntity(widgetId)
        roomEntity?.toBarcodeEntity()
    }

    override fun loadAll(): Observable<List<Barcode>> = Observable.fromCallable {
        val entities = roomBarcodeDao.loadAll()
        entities.map { it.toBarcodeEntity() }
    }

    override fun update(barcode: Barcode): Observable<Unit> = Observable.fromCallable {
        val roomEntity = RoomBarcode(barcode)
        roomBarcodeDao.update(roomEntity)
    }

    override fun eraseWidgetId(widgetId: Int): Observable<Int> = Observable.fromCallable {
        roomBarcodeDao.eraseWidgetId(widgetId)
    }

    override fun delete(barcode: Barcode): Observable<Int> = Observable.fromCallable {
        val roomEntity = RoomBarcode(barcode)
        roomBarcodeDao.delete(roomEntity)
    }
}
