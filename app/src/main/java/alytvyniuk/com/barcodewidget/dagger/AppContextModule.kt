package alytvyniuk.com.barcodewidget.dagger;

import android.app.Application
import android.content.Context
import androidx.annotation.NonNull
import dagger.Module
import dagger.Provides

import javax.inject.Singleton

@Module
class AppContextModule(private val application: Application) {

    @Provides
    @Singleton
    @NonNull
    fun providesContext() : Context {
        return application
    }
}
