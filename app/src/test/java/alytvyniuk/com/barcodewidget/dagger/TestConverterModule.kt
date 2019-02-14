package alytvyniuk.com.barcodewidget.dagger

import alytvyniuk.com.barcodewidget.converters.BarcodeToBitmap
import alytvyniuk.com.barcodewidget.converters.BitmapToBarcode
import alytvyniuk.com.barcodewidget.model.Barcode
import android.graphics.Bitmap
import androidx.annotation.NonNull
import dagger.Module
import dagger.Provides
import org.mockito.Mockito.mock
import javax.inject.Singleton


@Module
class TestConverterModule {

    companion object {
        val testBitmap = mock(Bitmap::class.java)!!
        val testBarcode = Barcode("Hello", "Test")
    }

    @Provides
    @NonNull
    @Singleton
    fun provideBarcodeToBitmap(): BarcodeToBitmap {
        return object : BarcodeToBitmap {
            override fun convert(barcode: Barcode): Bitmap {
                return testBitmap
            }
        }
    }

    @Provides
    @NonNull
    @Singleton
    fun provideBitmapToBarcode() : BitmapToBarcode = testBitmapToBarcodeStub()

    private fun testBitmapToBarcodeStub() : BitmapToBarcode = object : BitmapToBarcode {
        override fun convert(barcode: Bitmap): Barcode {
            return testBarcode
        }
    }
}
