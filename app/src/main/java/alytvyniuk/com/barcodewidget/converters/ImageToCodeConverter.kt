package alytvyniuk.com.barcodewidget.converters

import alytvyniuk.com.barcodewidget.model.Barcode
import android.graphics.Bitmap
import android.os.Handler
import java.lang.IllegalStateException
import javax.inject.Inject

class ImageToCodeConverter @Inject constructor(
    private val bitmapToBarcode: BitmapToBarcode,
    private val handler: Handler
) {

    interface BarcodeListener {
        fun onBarcodeConverted(barcode : Barcode)
    }

    private val postRunnable = object : Runnable {
        var resultBarcode : Barcode? = null

        override fun run() {
            listener?.onBarcodeConverted(resultBarcode!!)
            listener = null
        }

        fun setBarcode(barcode: Barcode) : Runnable {
            resultBarcode = barcode
            return this
        }
    }

    private var listener : BarcodeListener? = null

    @Synchronized
    fun convert(bitmap: Bitmap, barcodeListener: BarcodeListener) {
        if (this.listener != null) {
            throw IllegalStateException("BitmapToBarcodeConverter supports only one image at time")
        }
        this.listener = barcodeListener
        performConversion(bitmap)
    }

    private fun performConversion(bitmap: Bitmap) {
        val barcode = bitmapToBarcode.convert(bitmap)
        handler.post(postRunnable.setBarcode(barcode))
        listener?.onBarcodeConverted(barcode)
        listener = null
    }
}

