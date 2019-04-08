package alytvyniuk.com.barcodewidget

import alytvyniuk.com.barcodewidget.dagger.AppComponent
import alytvyniuk.com.barcodewidget.dagger.ComponentHolder
import android.app.Application


open class App : Application() {

    companion object {

        lateinit var component: AppComponent private set

        fun component() = component
    }

    override fun onCreate() {
        super.onCreate()
        component = ComponentHolder.getComponent(this)
    }
}
