package alytvyniuk.com.barcodewidget.dagger

import alytvyniuk.com.barcodewidget.converters.CodeToImageConverter
import alytvyniuk.com.barcodewidget.converters.ImageToCodeConverter
import alytvyniuk.com.barcodewidget.converters.ZXingCodeToImageConverter
import alytvyniuk.com.barcodewidget.converters.ZxingImageToCodeConverter
import dagger.Binds
import dagger.Module

@Module(includes = [MainLooperModule::class])
abstract class CodeToImageConverterModule {

    @Binds
    abstract fun provideCodeToImageConverter(codeToImageConverter: ZXingCodeToImageConverter) : CodeToImageConverter
}

@Module(includes = [MainLooperModule::class])
abstract class ImageToCodeConverterModule {

    @Binds
    abstract fun provideImageToCodeConverter(imageToCodeConverter: ZxingImageToCodeConverter) : ImageToCodeConverter
}