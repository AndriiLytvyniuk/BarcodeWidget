package alytvyniuk.com.barcodewidget.dagger

import alytvyniuk.com.barcodewidget.CaptureActivity
import alytvyniuk.com.barcodewidget.EditActivity
import alytvyniuk.com.barcodewidget.ListActivity
import alytvyniuk.com.barcodewidget.PreviewActivity
import androidx.work.WorkManager
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [ImageToCodeConverterModule::class,
        ImageToCodeConverterModule::class,
        CodeToImageConverterModule::class,
        ImageConverterFactoryModule::class,
        WorkManagerInitModule::class,
        BarcodeDaoModule::class]
)
interface AppComponent {

    fun inject(barcodeCaptureActivity: CaptureActivity)
    fun inject(editActivity: EditActivity)
    fun inject(previewActivity: PreviewActivity)
    fun inject(listActivity: ListActivity)

    fun getWorkManager() : WorkManager
}
