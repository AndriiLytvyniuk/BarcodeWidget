package alytvyniuk.com.barcodewidget.converters

import alytvyniuk.com.barcodewidget.model.Barcode
import android.graphics.Bitmap
import org.junit.Test

import org.junit.Assert.*
import org.mockito.Mockito.mock
import java.lang.IllegalStateException

class BarcodeToBitmapConverterTest {

    @Test
    fun convert() {
        val converter = BarcodeToBitmapConverter()
        val bitmapMock = mock(Bitmap::class.java)
        val barcodeMock = Barcode()
        converter.convert(barcodeMock, object: BarcodeToBitmapConverter.BitmapListener {
            override fun onBitmapConverted(bitmap: Bitmap) {
                assertEquals(bitmapMock, bitmap)
            }
        })
        converter.sendResult(bitmapMock)
        assertNull(converter.listener)
    }

    @Test(expected = IllegalStateException::class)
    fun convertTestMultiple() {
        val converter = BarcodeToBitmapConverter()
        val barcode = Barcode()
        converter.convert(barcode, mock(BarcodeToBitmapConverter.BitmapListener::class.java))
        converter.convert(barcode, mock(BarcodeToBitmapConverter.BitmapListener::class.java))
    }
}