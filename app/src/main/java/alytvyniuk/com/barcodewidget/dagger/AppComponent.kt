package alytvyniuk.com.barcodewidget.dagger

import alytvyniuk.com.barcodewidget.BarcodeCaptureActivity
import alytvyniuk.com.barcodewidget.TestCaptureActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [ImageToCodeConverterModule::class,
        CodeToImageConverterModule::class,
        FileStorageModule::class]
)
interface AppComponent {

    fun inject(testCaptureActivity: TestCaptureActivity)
    fun inject(barcodeCaptureActivity: BarcodeCaptureActivity)
}