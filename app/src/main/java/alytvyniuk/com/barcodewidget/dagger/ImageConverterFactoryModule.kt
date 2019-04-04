package alytvyniuk.com.barcodewidget.dagger

import alytvyniuk.com.barcodewidget.ImageConvertModelFactory
import alytvyniuk.com.barcodewidget.converters.ZxingImageToCodeConverter
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [ImageToCodeConverterModule::class])
class ImageConverterFactoryModule {

    @Provides
    @Singleton
    fun provideCodeToImageConverter(imageToCodeConverter: ZxingImageToCodeConverter) : ImageConvertModelFactory {
        return ImageConvertModelFactory(imageToCodeConverter)
    }
}