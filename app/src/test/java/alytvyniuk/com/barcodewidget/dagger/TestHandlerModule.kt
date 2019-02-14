package alytvyniuk.com.barcodewidget.dagger

import android.os.Handler
import android.os.Looper
import android.os.Message
import androidx.annotation.NonNull
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import dagger.Module
import dagger.Provides
import org.mockito.ArgumentMatchers.any
import javax.inject.Singleton
import org.mockito.invocation.InvocationOnMock
import org.mockito.stubbing.Answer



@Module
class TestHandlerModule {

    @Provides
    @Singleton
    @NonNull
    fun provideMainHandler() : Handler {
        val handler = mock<Handler>()
        whenever(handler.post(any(Runnable::class.java))).thenAnswer(object : Answer<Boolean> {
            override fun answer(invocation: InvocationOnMock?): Boolean {
                val runnable = invocation?.getArgument<Runnable>(0)
                runnable?.run()
                return true
            }
        })

        return handler
    }
}