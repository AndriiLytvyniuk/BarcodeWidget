package alytvyniuk.com.barcodewidget

import alytvyniuk.com.barcodewidget.converters.ZxingImageToCodeConverter
import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.platform.app.InstrumentationRegistry
import testutils.captureValues
import kotlinx.coroutines.*
import org.junit.Assert.assertNotNull
import org.junit.Test

import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class ImageConvertViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var subject: ImageConvertViewModel

    @Before
    fun setup() {
        val application = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as Application
        subject = ImageConvertViewModel(application, ZxingImageToCodeConverter())
    }


    //@Test
    fun performConversionTest() {
        runBlocking {
            subject.liveData.captureValues {
                subject.performConversion(Uri.parse("error_uri"))
                Log.d("Andrii", ": " + subject.liveData.value)
                assertNotNull(subject.liveData.value!!.exception)
            }
        }
    }
}
