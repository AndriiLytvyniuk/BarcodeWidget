package alytvyniuk.com.barcodewidget.dagger

import alytvyniuk.com.barcodewidget.converters.CodeToImageConverterTest
import alytvyniuk.com.barcodewidget.converters.ImageToCodeConverterTest
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [TestConverterModule::class, TestHandlerModule::class])
interface TestConverterComponent : ConverterComponent {

    fun inject(codeToImageConverterTest: CodeToImageConverterTest)

    fun inject(imageToCodeConverterTest: ImageToCodeConverterTest)
}