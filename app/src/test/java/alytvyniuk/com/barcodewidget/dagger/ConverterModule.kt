package alytvyniuk.com.barcodewidget.dagger

import alytvyniuk.com.barcodewidget.converters.BarcodeToBitmap
import alytvyniuk.com.barcodewidget.model.Barcode
import android.graphics.Bitmap
import androidx.annotation.NonNull
import dagger.Module
import dagger.Provides
import org.mockito.Mockito.mock
import javax.inject.Singleton


@Module
class ConverterModule {

    companion object {
        val testBitmap = mock(Bitmap::class.java)!!
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
}
