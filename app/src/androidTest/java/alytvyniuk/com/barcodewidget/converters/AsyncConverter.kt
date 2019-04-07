package alytvyniuk.com.barcodewidget.converters

import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.SparseArray
import androidx.annotation.VisibleForTesting
import java.lang.Exception

private const val MESSAGE_RESULT = 100
private const val RESULT_OK = 0
private const val RESULT_FAILURE = 1

abstract class AsyncConverter<FROM, TO : Any>(
    looper: Looper
) {

    private val handler = createConverterHandler(looper)
    @VisibleForTesting
    val listeners: SparseArray<ConverterListener<TO>> = SparseArray()

    @VisibleForTesting
    fun createConverterHandler(looper: Looper): Handler = ConverterHandler(looper)

    @Synchronized
    fun convertAsync(from: FROM, listener: ConverterListener<TO>) {
        val key = addListener(listener)
        performConversion(from, key)
    }

    protected abstract fun performConversion(from: FROM, id: Int)

    @Synchronized
    protected fun sendResult(to: TO, id: Int) {
        val message = getMessage(id, to, RESULT_OK)
        handler.sendMessage(message)
    }

    @Synchronized
    protected fun sendError(exception: Exception, id: Int) {
        val message = getMessage(id, exception, RESULT_FAILURE)
        handler.sendMessage(message)
    }

    @Synchronized
    private fun getNextKey(): Int {
        val size = listeners.size()
        return if (size > 0) listeners.keyAt(size - 1) + 1 else 0
    }

    private fun getMessage(id: Int, result: Any, returnType: Int): Message {
        val message = handler.obtainMessage()
        message.what = MESSAGE_RESULT
        message.arg1 = id
        message.arg2 = returnType
        message.obj = result
        return message
    }

    @Synchronized
    @VisibleForTesting
    fun addListener(listener: ConverterListener<TO>): Int {
        val key = getNextKey()
        listeners.put(key, listener)
        return key
    }


    @Synchronized
    @VisibleForTesting
    fun removeListener(id: Int): ConverterListener<TO> {
        val listener = listeners.get(id)
        listeners.remove(id)
        return listener
    }

    interface ConverterListener<RESULT> {

        fun onResult(to: RESULT)

        fun onError(exception: Exception)
    }

    inner class ConverterHandler(looper: Looper) : Handler(looper) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (msg.what == MESSAGE_RESULT) {
                val id = msg.arg1
                val listener = removeListener(id)
                val resultCode = msg.arg2
                when (resultCode) {
                    RESULT_OK -> listener.onResult(msg.obj as TO)
                    RESULT_FAILURE -> listener.onError(msg.obj as Exception)
                }
            }
        }
    }
}
