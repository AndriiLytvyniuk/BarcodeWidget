package alytvyniuk.com.barcodewidget.converters

import alytvyniuk.com.barcodewidget.model.Barcode
import android.graphics.Bitmap
import org.junit.Test

import org.junit.Assert.*
import org.mockito.Mockito.mock
import java.lang.IllegalStateException

class BitmapToBarcodeConverterTest {

    @Test
    fun convert() {
        val converter = ImageToCodeConverter()
        val bitmapMock = mock(Bitmap::class.java)
        val barcodeMock = Barcode()
        converter.convert(bitmapMock, object: ImageToCodeConverter.BarcodeListener {
            override fun onBarcodeConverted(barcode: Barcode) {
                assertEquals(barcodeMock, barcode)
            }
        })
        converter.sendResult(barcodeMock)
        assertNull(converter.listener)
    }

    @Test(expected = IllegalStateException::class)
    fun convertTestMultiple() {
        val converter = ImageToCodeConverter()
        val bitmapMock = mock(Bitmap::class.java)
        converter.convert(bitmapMock, mock(ImageToCodeConverter.BarcodeListener::class.java))
        converter.convert(bitmapMock, mock(ImageToCodeConverter.BarcodeListener::class.java))
    }
}