package alytvyniuk.com.barcodewidget.dagger

import alytvyniuk.com.barcodewidget.converters.AsyncConverterTest
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [MainLooperModule::class]
)
interface TestAppComponent {

    fun inject(asyncConverterTest: AsyncConverterTest)
}