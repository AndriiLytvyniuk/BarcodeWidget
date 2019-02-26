package alytvyniuk.com.barcodewidget

import android.app.Application
import android.util.Log
import com.google.firebase.FirebaseApp

class BarcodeApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Log.d("Andrii", ": onCreate")
        FirebaseApp.initializeApp(this)
    }
}