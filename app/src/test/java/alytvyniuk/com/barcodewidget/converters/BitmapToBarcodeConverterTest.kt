package alytvyniuk.com.barcodewidget.converters

import alytvyniuk.com.barcodewidget.model.Barcode
import android.graphics.Bitmap
import org.junit.Test

import org.junit.Assert.*
import org.mockito.Mockito.mock

class BitmapToBarcodeConverterTest {

    @Test
    fun convert() {
        val converter = BitmapToBarcodeConverter()
        val bitmapMock = mock(Bitmap::class.java)
        val barcodeMock = Barcode()
        converter.convert(bitmapMock, object: BitmapToBarcodeConverter.BarcodeListener {
            override fun onBarcodeConverted(barcode: Barcode) {
                assertEquals(barcodeMock, barcode)
            }
        })
        converter.sendResult(barcodeMock)
        assertNull(converter.barcodeListener)
    }
}