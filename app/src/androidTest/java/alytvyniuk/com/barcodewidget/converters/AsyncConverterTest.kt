package alytvyniuk.com.barcodewidget.converters

import alytvyniuk.com.barcodewidget.App
import alytvyniuk.com.barcodewidget.dagger.DaggerTestAppComponent
import android.os.Looper
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
class AsyncConverterTest {

    @Inject lateinit var looper: Looper
    lateinit var asyncConverter: TestAsyncConverter

    @Before
    fun before() {
        (App.component() as DaggerTestAppComponent).inject(this)
        asyncConverter = TestAsyncConverter(looper)
    }

    @Test
    fun testListenersStructure() {
        val l1 = ConverterListenerStub()
        val l2 = ConverterListenerStub()
        val l3 = ConverterListenerStub()
        val l4 = ConverterListenerStub()
        val id1 = asyncConverter.addListener(l1)
        val id2 = asyncConverter.addListener(l2)
        var l = asyncConverter.removeListener(id2)
        var size = asyncConverter.listeners.size()
        assertEquals(l2, l)
        assertEquals(size, 1)
        val id3 = asyncConverter.addListener(l3)
        val id4 = asyncConverter.addListener(l4)
        l = asyncConverter.removeListener(id4)
        size = asyncConverter.listeners.size()
        assertEquals(l4, l)
        assertEquals(size, 2)
        l = asyncConverter.removeListener(id1)
        size = asyncConverter.listeners.size()
        assertEquals(l1, l)
        assertEquals(size, 1)
        l = asyncConverter.removeListener(id3)
        size = asyncConverter.listeners.size()
        assertEquals(l3, l)
        assertEquals(size, 0)
    }

    class ConverterListenerStub : AsyncConverter.ConverterListener<Any> {
        override fun onResult(to: Any) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onError(exception: Exception) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

    }

    class TestAsyncConverter(looper: Looper) : AsyncConverter<Any, Any>(looper) {

        override fun performConversion(from: Any, id: Int) {

        }
    }
}
