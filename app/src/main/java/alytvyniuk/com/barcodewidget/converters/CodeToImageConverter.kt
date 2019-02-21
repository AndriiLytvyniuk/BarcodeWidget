package alytvyniuk.com.barcodewidget.converters

import alytvyniuk.com.barcodewidget.model.Barcode
import android.graphics.Bitmap
import android.os.Handler
import javax.inject.Inject


class CodeToImageConverter @Inject constructor(handler: Handler,
                                               private val barcodeToBitmap: BarcodeToBitmap)
    : AsyncConverter<Barcode, Bitmap>(handler) {

    override fun performConversion(from: Barcode) {
        val result = barcodeToBitmap.convert(from)
        sendResult(result)
    }
}