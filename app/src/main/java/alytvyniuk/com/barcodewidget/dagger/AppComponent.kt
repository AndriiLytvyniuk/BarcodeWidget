package alytvyniuk.com.barcodewidget.dagger

import alytvyniuk.com.barcodewidget.TestCaptureActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ImageToCodeConverterModule::class, CodeToImageConverterModule::class])
interface AppComponent {

    fun inject(testCaptureActivity: TestCaptureActivity)
}