package alytvyniuk.com.barcodewidget.dagger

import alytvyniuk.com.barcodewidget.CaptureActivity
import alytvyniuk.com.barcodewidget.EditActivity
import alytvyniuk.com.barcodewidget.ListActivity
import alytvyniuk.com.barcodewidget.PreviewActivity
import alytvyniuk.com.barcodewidget.WidgetUpdateService
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [ImageToCodeConverterModule::class,
        ImageToCodeConverterModule::class,
        CodeToImageConverterModule::class,
        ImageConverterFactoryModule::class,
        BarcodeDaoModule::class]
)
interface AppComponent {

    fun inject(barcodeCaptureActivity: CaptureActivity)
    fun inject(widgetUpdateService: WidgetUpdateService)
    fun inject(editActivity: EditActivity)
    fun inject(previewActivity: PreviewActivity)
    fun inject(listActivity: ListActivity)
}
