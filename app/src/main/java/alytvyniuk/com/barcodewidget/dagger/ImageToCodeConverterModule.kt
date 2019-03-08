package alytvyniuk.com.barcodewidget.dagger

import alytvyniuk.com.barcodewidget.converters.ImageToCodeConverter
import alytvyniuk.com.barcodewidget.converters.StubImageToCodeConverter
import alytvyniuk.com.barcodewidget.converters.ZxingImageToCodeConverter
import android.os.Looper
import androidx.annotation.NonNull
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [MainLooperModule::class])
open class ImageToCodeConverterModule {

//    @Provides
//    @NonNull
//    @Singleton
//    fun provideImageToCodeConverter(looper: Looper) : ImageToCodeConverter {
//        return StubImageToCodeConverter(looper)
//    }

    @Provides
    @NonNull
    @Singleton
    fun provideImageToCodeConverter(looper: Looper) : ImageToCodeConverter {
        return ZxingImageToCodeConverter(looper)
    }
}