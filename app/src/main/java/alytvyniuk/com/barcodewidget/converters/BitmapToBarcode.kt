package alytvyniuk.com.barcodewidget.converters

import alytvyniuk.com.barcodewidget.model.RawBarcode
import android.graphics.Bitmap

interface BitmapToBarcode {

    fun convert(bitmap: Bitmap) : RawBarcode
}