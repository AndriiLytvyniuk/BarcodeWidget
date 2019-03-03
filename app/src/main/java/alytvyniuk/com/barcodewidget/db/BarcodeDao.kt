package alytvyniuk.com.barcodewidget.db

import alytvyniuk.com.barcodewidget.model.Barcode
import javax.inject.Inject

class BarcodeDao @Inject constructor(private val roomBarcodeDao: RoomBarcodeDao) {

    fun insert(barcode: Barcode, widgetId: Int) {
        val barcodeEntity = barcode.toBarcodeEntity(widgetId)
        roomBarcodeDao.insert(barcodeEntity)
    }

    fun loadBarcodeEntity(widgetId: Int) : Barcode {
        val barcodeEntity = roomBarcodeDao.loadBarcodeEntity(widgetId)
        return barcodeEntity.toBarcode()
    }
}