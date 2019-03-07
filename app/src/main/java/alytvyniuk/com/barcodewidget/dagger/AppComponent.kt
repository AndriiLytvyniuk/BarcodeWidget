package alytvyniuk.com.barcodewidget.dagger

import alytvyniuk.com.barcodewidget.*
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
    fun inject(barcodeCaptureActivity: CaptureActivity)
    fun inject(barcodeWidgetProvider: BarcodeWidgetProvider)
    fun inject(editActivity: EditActivity)
    fun inject(previewActivity: PreviewActivity)
    fun inject(barcodeListActivity: BarcodeListActivity)
}