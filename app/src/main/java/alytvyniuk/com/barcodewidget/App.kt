package alytvyniuk.com.barcodewidget

import alytvyniuk.com.barcodewidget.dagger.AppComponent
import alytvyniuk.com.barcodewidget.dagger.AppContextModule
import alytvyniuk.com.barcodewidget.dagger.DaggerAppComponent
import android.app.Application



class App : Application() {

    companion object {
        private lateinit var component : AppComponent
        fun component() = component
    }

    override fun onCreate() {
        super.onCreate()
        component = DaggerAppComponent.builder().appContextModule(AppContextModule(this)).build()
    }
}