package alytvyniuk.com.barcodewidget.dagger;

import android.os.Looper
import androidx.annotation.NonNull
import dagger.Module
import dagger.Provides

import javax.inject.Singleton

@Module
class MainLooperModule {

    @Provides
    @Singleton
    @NonNull
    fun providesMainLooper() : Looper {
        return Looper.getMainLooper()
    }
}
