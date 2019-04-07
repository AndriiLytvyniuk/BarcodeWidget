package alytvyniuk.com.barcodewidget.dagger

import android.app.Application
import androidx.annotation.NonNull
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppContextModule(private val application: Application) {

    @Provides
    @Singleton
    @NonNull
    fun providesContext() : Application {
        return application
    }
}
