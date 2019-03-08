package alytvyniuk.com.barcodewidget.dagger

import alytvyniuk.com.barcodewidget.converters.CodeToImageConverter
import alytvyniuk.com.barcodewidget.converters.ZXingCodeToImageConverter
import android.os.Looper
import androidx.annotation.NonNull
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [MainLooperModule::class])
open class CodeToImageConverterModule {

//    @Provides
//    @NonNull
//    @Singleton
//    fun provideCodeToImageConverter(looper: Looper, context: Context) : CodeToImageConverter {
//        return StubCodeToImageConverter(looper, context)
//    }

    @Provides
    @NonNull
    @Singleton
    fun provideCodeToImageConverter(looper: Looper) : CodeToImageConverter {
        return ZXingCodeToImageConverter(looper)
    }
}