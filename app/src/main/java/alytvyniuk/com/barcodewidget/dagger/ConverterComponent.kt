package alytvyniuk.com.barcodewidget.dagger

import alytvyniuk.com.barcodewidget.BarcodeCaptureActivity
import alytvyniuk.com.barcodewidget.converters.CodeToImageConverter
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ConverterModule::class])
interface ConverterComponent {

    fun barcodeToBitmapConverter() : CodeToImageConverter

    fun inject(barcodeCaptureActivity: BarcodeCaptureActivity)
}