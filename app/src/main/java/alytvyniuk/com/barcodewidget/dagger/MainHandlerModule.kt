package alytvyniuk.com.barcodewidget.dagger;

import android.os.Handler
import android.os.Looper
import androidx.annotation.NonNull
import dagger.Module
import dagger.Provides

import javax.inject.Singleton

@Module
class MainHandlerModule {

    @Provides
    @Singleton
    @NonNull
    fun provideMainHandler() : Handler {
        return Handler(Looper.getMainLooper())
    }
}
