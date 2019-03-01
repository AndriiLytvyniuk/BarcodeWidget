package alytvyniuk.com.barcodewidget.dagger;

import alytvyniuk.com.barcodewidget.FileStorage
import android.content.Context
import androidx.annotation.NonNull
import dagger.Module
import dagger.Provides

import javax.inject.Singleton

@Module(includes = [AppContextModule::class])
class FileStorageModule {

    @Provides
    @Singleton
    @NonNull
    fun providesFileStorage(context: Context) : FileStorage {
        return FileStorage(context)
    }
}
