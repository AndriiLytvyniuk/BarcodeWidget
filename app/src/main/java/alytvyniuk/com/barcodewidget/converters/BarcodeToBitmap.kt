package alytvyniuk.com.barcodewidget.converters

import alytvyniuk.com.barcodewidget.model.Barcode
import android.graphics.Bitmap

interface BarcodeToBitmap {

    fun convert(barcode: Barcode) : Bitmap
}