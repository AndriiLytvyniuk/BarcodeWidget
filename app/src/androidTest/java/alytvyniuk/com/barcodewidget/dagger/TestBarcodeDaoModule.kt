package alytvyniuk.com.barcodewidget.dagger

import alytvyniuk.com.barcodewidget.db.BarcodeDao
import alytvyniuk.com.barcodewidget.model.Barcode
import dagger.Module
import dagger.Provides
import io.reactivex.Observable
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


    override fun insert(barcode: Barcode): Observable<Unit> = Observable.fromCallable {
        barcodes.add(barcode)
        return@fromCallable
    }

    override fun loadBarcodeEntities(widgetIds: List<Int>): Observable<List<Barcode>> = Observable.fromCallable {
        barcodes.subList(0, widgetIds.size)
    }

    override fun loadAll(): Observable<List<Barcode>> = Observable.fromCallable {
        barcodes
    }

    override fun update(barcode: Barcode): Observable<Unit> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun eraseWidgetIds(widgetIds: List<Int>): Observable<Int> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun delete(barcode: Barcode): Observable<Int> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

