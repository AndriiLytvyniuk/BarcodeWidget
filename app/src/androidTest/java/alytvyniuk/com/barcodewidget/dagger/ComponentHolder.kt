package alytvyniuk.com.barcodewidget.dagger

import android.app.Application

object ComponentHolder {

    fun getComponent(application: Application) : AppComponent {
        return DaggerTestAppComponent.builder().build()
    }
}