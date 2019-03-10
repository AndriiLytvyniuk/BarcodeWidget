package alytvyniuk.com.barcodewidget.converters

import alytvyniuk.com.barcodewidget.model.RawBarcode
import android.graphics.Bitmap

interface BarcodeToBitmap {

    fun convert(rawBarcode: RawBarcode) : Bitmap
}