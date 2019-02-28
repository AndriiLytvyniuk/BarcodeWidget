package alytvyniuk.com.barcodewidget.dagger

import alytvyniuk.com.barcodewidget.converters.*
import android.os.Looper
import androidx.annotation.NonNull
import com.google.firebase.FirebaseApp
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [MainLooperModule::class, FirebaseInitModule::class])
open class ImageToCodeConverterModule {

    @Provides
    @NonNull
    @Singleton
    fun provideImageToCodeConverter(looper: Looper, firebaseApp: FirebaseApp) : ImageToCodeConverter {
        return MLKitImageToCodeConverter(looper)
    }
}