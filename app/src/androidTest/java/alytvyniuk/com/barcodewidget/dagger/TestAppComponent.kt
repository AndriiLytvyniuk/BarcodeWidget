package alytvyniuk.com.barcodewidget.dagger

import alytvyniuk.com.barcodewidget.ListActivityTest
import alytvyniuk.com.barcodewidget.converters.AsyncConverterTest
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [MainLooperModule::class,
        TestBarcodeDaoModule::class,
        ImageConverterFactoryModule::class,
        CodeToImageConverterModule::class]
)
interface TestAppComponent : AppComponent {

    fun inject(asyncConverterTest: AsyncConverterTest)

    fun inject(listActivityTest: ListActivityTest)
}