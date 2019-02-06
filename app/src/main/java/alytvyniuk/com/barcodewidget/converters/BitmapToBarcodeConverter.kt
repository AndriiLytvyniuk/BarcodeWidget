package alytvyniuk.com.barcodewidget.converters

import alytvyniuk.com.barcodewidget.model.Barcode
import android.graphics.Bitmap
import androidx.annotation.VisibleForTesting
import java.lang.IllegalStateException

class BitmapToBarcodeConverter {

    interface BarcodeListener {
        fun onBarcodeConverted(barcode : Barcode)
    }

    @VisibleForTesting
    var barcodeListener : BarcodeListener? = null

    @Synchronized
    fun convert(bitmap: Bitmap, barcodeListener: BarcodeListener) {
        if (this.barcodeListener != null) {
            throw IllegalStateException("BitmapToBarcodeConverter supports only one image at time")
        }
        this.barcodeListener = barcodeListener
    }

    @VisibleForTesting
    fun sendResult(barcode: Barcode) {
        val barcodeListener = this.barcodeListener
        this.barcodeListener = null
        barcodeListener!!.onBarcodeConverted(barcode)
    }
}

