package alytvyniuk.com.barcodewidget.dagger;

import android.content.Context
import androidx.annotation.NonNull
import com.google.firebase.FirebaseApp
import dagger.Module
import dagger.Provides

import javax.inject.Singleton

@Module(includes = [AppContextModule::class])
class FirebaseInitModule {

    @Provides
    @Singleton
    @NonNull
    fun providesContext(context: Context) : FirebaseApp {
        return FirebaseApp.initializeApp(context)!!
    }
}
