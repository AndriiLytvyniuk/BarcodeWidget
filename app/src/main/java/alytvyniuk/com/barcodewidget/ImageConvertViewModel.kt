package alytvyniuk.com.barcodewidget

import alytvyniuk.com.barcodewidget.converters.ImageToCodeConverter
import alytvyniuk.com.barcodewidget.model.RawBarcode
import android.app.Application
import android.net.Uri
import androidx.annotation.NonNull
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ImageConvertViewModel(application: Application,
                            private val imageToCodeConverter: ImageToCodeConverter) : AndroidViewModel(application) {

    @VisibleForTesting
    val liveData: MutableLiveData<ConvertResponse> = MutableLiveData()

    fun observe(@NonNull owner: LifecycleOwner, @NonNull observer: Observer<ConvertResponse>) {
        liveData.observe(owner, observer)
    }

    @SuppressWarnings("TooGenericExceptionCaught")
    fun performConversion(uri: Uri) {
        viewModelScope.launch {
            try {
                val bitmap = getApplication<Application>().getBitmapForImageUri(uri)
                val rawBarcode = imageToCodeConverter.convert(bitmap)
                liveData.value = ConvertResponse(rawBarcode)
            } catch (e : Exception) {
                liveData.value = ConvertResponse(exception = e)
            }
        }
    }
}

class ImageConvertModelFactory(private val application: Application,
                               private val imageToCodeConverter: ImageToCodeConverter) : ViewModelProvider.Factory {
    @SuppressWarnings("unchecked")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ImageConvertViewModel(application, imageToCodeConverter) as T
    }
}

data class ConvertResponse(val barcode: RawBarcode? = null, val exception: Throwable? = null)
