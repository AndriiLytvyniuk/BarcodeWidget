package alytvyniuk.com.barcodewidget.dagger

import alytvyniuk.com.barcodewidget.converters.CodeToImageConverterTest
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [TestConverterModule::class])
interface TestConverterComponent : ConverterComponent {

    fun inject(barcodeToBitmapConverterTest: CodeToImageConverterTest)
}