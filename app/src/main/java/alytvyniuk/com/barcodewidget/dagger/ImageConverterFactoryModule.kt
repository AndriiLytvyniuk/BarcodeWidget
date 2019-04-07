package alytvyniuk.com.barcodewidget.dagger

import alytvyniuk.com.barcodewidget.ImageConvertModelFactory
import alytvyniuk.com.barcodewidget.converters.ZxingImageToCodeConverter
import android.app.Application
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [ImageToCodeConverterModule::class, AppContextModule::class])
class ImageConverterFactoryModule {

    @Provides
    @Singleton
    fun provideCodeToImageConverter(application: Application, imageToCodeConverter: ZxingImageToCodeConverter) : ImageConvertModelFactory {
        return ImageConvertModelFactory(application, imageToCodeConverter)
    }
}
