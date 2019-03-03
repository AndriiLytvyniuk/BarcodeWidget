package alytvyniuk.com.barcodewidget.dagger

import alytvyniuk.com.barcodewidget.BarcodeCaptureActivity
import alytvyniuk.com.barcodewidget.BarcodeWidgetProvider
import alytvyniuk.com.barcodewidget.TestCaptureActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [ImageToCodeConverterModule::class,
        CodeToImageConverterModule::class,
        FileStorageModule::class,
            BarcodeDaoModule::class]
)
interface AppComponent {

    fun inject(testCaptureActivity: TestCaptureActivity)
    fun inject(barcodeCaptureActivity: BarcodeCaptureActivity)
    fun inject(barcodeWidgetProvider: BarcodeWidgetProvider)
}