package alytvyniuk.com.barcodewidget.converters

import alytvyniuk.com.barcodewidget.model.Barcode
import android.graphics.Bitmap

interface BitmapToBarcode {

    fun convert(barcode: Bitmap) : Barcode
}