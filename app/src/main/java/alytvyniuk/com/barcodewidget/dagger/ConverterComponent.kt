package alytvyniuk.com.barcodewidget.dagger

import alytvyniuk.com.barcodewidget.converters.BarcodeToBitmapConverter
import androidx.annotation.NonNull
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ConverterModule::class])
interface ConverterComponent {

    companion object {
        fun barcodeToBitmap() : ConverterComponent {
            return DaggerConverterComponent.builder().converterModule(ConverterModule()).build()
        }
    }

    fun inject(@NonNull barcodeToBitmapConverter : BarcodeToBitmapConverter)
}