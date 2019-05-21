package alytvyniuk.com.barcodewidget

import alytvyniuk.com.barcodewidget.converters.ImageToCodeConverter
import alytvyniuk.com.barcodewidget.converters.ZxingImageToCodeConverter
import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.platform.app.InstrumentationRegistry
import com.example.android.kotlincoroutines.test.util.captureValues
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.lang.Exception

@RunWith(JUnit4::class)
class ImageConvertViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var subject: ImageConvertViewModel
    //private val testDispatcher = TestCoroutineDispatcher()

    @Before
    fun setup() {
        //Dispatchers.setMain(testDispatcher)
        val application = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as Application
        subject = ImageConvertViewModel(application, ZxingImageToCodeConverter())
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
        //testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun performConversionTest() {
        runBlocking {
            subject.liveData.captureValues {
                subject.performConversion(Uri.parse("error_uri"))
                assertNotNull(subject.liveData.value!!.exception)
            }
        }
    }


}