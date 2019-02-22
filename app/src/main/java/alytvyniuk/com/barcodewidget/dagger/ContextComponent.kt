package alytvyniuk.com.barcodewidget.dagger

import alytvyniuk.com.barcodewidget.BarcodeCaptureActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ConverterModule::class, MainHandlerModule::class])
interface ConverterComponent {

    fun inject(barcodeCaptureActivity: BarcodeCaptureActivity)
}