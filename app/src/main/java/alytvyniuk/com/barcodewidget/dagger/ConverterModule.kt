package alytvyniuk.com.barcodewidget.dagger

import alytvyniuk.com.barcodewidget.converters.BarcodeToBitmap
import alytvyniuk.com.barcodewidget.model.Barcode
import android.graphics.Bitmap
import androidx.annotation.NonNull
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
open class ConverterModule {

    @Provides
    @NonNull
    @Singleton
    fun provideBarcodeToBitmap() : BarcodeToBitmap = returnReleaseStub()

    private fun returnReleaseStub() : BarcodeToBitmap = object : BarcodeToBitmap {
        override fun convert(barcode: Barcode): Bitmap {
            val w = 300
            val h = 300
            val conf = Bitmap.Config.ARGB_8888 // see other conf types
            return Bitmap.createBitmap(w, h, conf)
        }
    }
}