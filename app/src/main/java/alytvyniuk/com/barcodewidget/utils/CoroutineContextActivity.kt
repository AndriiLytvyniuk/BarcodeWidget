package alytvyniuk.com.barcodewidget.utils

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

abstract class CoroutineContextActivity : AppCompatActivity(), CoroutineScope {

    private lateinit var mJob: Job
    override val coroutineContext: CoroutineContext
        get() = mJob + Dispatchers.Main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mJob = Job()
    }

//    fun <T> launchWithResult (blockAsync: suspend CoroutineScope.() -> T,
//                         blockUI: suspend CoroutineScope.(T) -> Unit) {
//        launch {
//            val deferred = async { blockAsync }
//            val result = deferred.await()
//            blockUI(result)
//        }
//    }
//    private val reusableDisposable = ReusableCompositeDisposable()
//
//    protected fun getReusableCompositeDisposable() = reusableDisposable
//
//    protected fun addDisposable(disposable: Disposable) {
//        reusableDisposable.add(disposable)
//    }
//
//    override fun onStop() {
//        super.onStop()
//        reusableDisposable.dispose()
//    }

    override fun onDestroy() {
        super.onDestroy()
        mJob.cancel()
    }
}
