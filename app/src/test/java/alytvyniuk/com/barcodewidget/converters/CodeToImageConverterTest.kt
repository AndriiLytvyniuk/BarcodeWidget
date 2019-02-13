package alytvyniuk.com.barcodewidget.converters


import alytvyniuk.com.barcodewidget.dagger.DaggerTestConverterComponent
import alytvyniuk.com.barcodewidget.dagger.TestConverterModule
import alytvyniuk.com.barcodewidget.model.Barcode
import android.graphics.Bitmap
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

import javax.inject.Inject

class CodeToImageConverterTest {

    @Inject lateinit var converter: CodeToImageConverter

    @Before
    fun before() {
        DaggerTestConverterComponent.create().inject(this)
    }

    @Test
    fun convertTest() {
        val bitmapMock = TestConverterModule.testBitmap
        val barcodeMock = Barcode()
        val b = object: CodeToImageConverter.BitmapListener {
            override fun onBitmapConverted(bitmap: Bitmap) {
                assertEquals(bitmapMock, bitmap)
            }
        }
        converter.convert(barcodeMock, b)
        assertNull(converter.listener)
    }
}