package alytvyniuk.com.barcodewidget.converters

import alytvyniuk.com.barcodewidget.model.Barcode
import android.graphics.Bitmap
import android.os.Handler
import javax.inject.Inject

class ImageToCodeConverter @Inject constructor(handler: Handler,
                           private val bitmapToBarcode: BitmapToBarcode)
    : AsyncConverter<Bitmap, Barcode>(handler) {

    override fun performConversion(from: Bitmap) {
        val result = bitmapToBarcode.convert(from)
        sendResult(result)
    }
}