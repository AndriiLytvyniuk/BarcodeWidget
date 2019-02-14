package alytvyniuk.com.barcodewidget.converters

import alytvyniuk.com.barcodewidget.dagger.AsyncConverter
import alytvyniuk.com.barcodewidget.dagger.DaggerTestConverterComponent
import alytvyniuk.com.barcodewidget.dagger.TestConverterModule
import alytvyniuk.com.barcodewidget.model.Barcode
import android.graphics.Bitmap
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.mockito.Mockito.mock
import javax.inject.Inject

class ImageToCodeConverterTest {

    @Inject
    lateinit var converter: ImageToCodeConverter

    @Before
    fun before() {
        DaggerTestConverterComponent.create().inject(this)
    }

    @Test
    fun convert() {
        val bitmapMock = mock(Bitmap::class.java)
        val b = object: AsyncConverter.ConverterListener<Barcode> {
            override fun onResult(to: Barcode) {
                assertEquals(TestConverterModule.testBarcode, to)
            }
        }
        converter.convert(bitmapMock, b)
        assertNull(converter.listener)
    }
}