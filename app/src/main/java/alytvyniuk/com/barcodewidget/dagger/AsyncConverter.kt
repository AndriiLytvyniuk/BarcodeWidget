package alytvyniuk.com.barcodewidget.dagger

import android.os.Handler
import androidx.annotation.VisibleForTesting

abstract class AsyncConverter<FROM, TO> (
    val handler: Handler
) {

    interface ConverterListener<RESULT> {

        fun onResult(to : RESULT)
    }

    @VisibleForTesting
    var listener : ConverterListener<TO>? = null

    private val postRunnable = object : Runnable {
        private var result : TO? = null
        private var l : ConverterListener<TO>? = null

        override fun run() {
            l?.onResult(result!!)
            l = null
        }

        fun setResult(r: TO, listener : ConverterListener<TO>) : Runnable {
            result = r
            l = listener
            return this
        }
    }

    @Synchronized
    fun convert(from : FROM, listener: ConverterListener<TO>) {
        this.listener = listener
        performConversion(from)
    }

    protected abstract fun performConversion(from: FROM)

    @Synchronized
    protected fun sendResult(to : TO) {
        handler.post(postRunnable.setResult(to, listener!!))
        listener = null
    }
}