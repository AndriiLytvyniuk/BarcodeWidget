package alytvyniuk.com.barcodewidget.utils

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

abstract class CoroutineScopeActivity : AppCompatActivity(), AsyncTaskCoroutineScope {

    private lateinit var mJob: Job
    override val coroutineContext: CoroutineContext
        get() = mJob + Dispatchers.Main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mJob = Job()
    }

    override fun onDestroy() {
        super.onDestroy()
        mJob.cancel()
    }
}

interface AsyncTaskCoroutineScope : CoroutineScope {

    fun <T> launchWithResult (blockAsync: suspend () -> T, blockUI: (T) -> Unit) {
        launch {
            val deferred = async(Dispatchers.Default) {
                blockAsync()
            }
            val res = deferred.await()
            blockUI(res)
        }
    }
}
