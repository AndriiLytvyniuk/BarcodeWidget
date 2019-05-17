package alytvyniuk.com.barcodewidget

import alytvyniuk.com.barcodewidget.converters.ImageToCodeConverter
import alytvyniuk.com.barcodewidget.model.RawBarcode
import android.app.Application
import android.net.Uri
import androidx.annotation.NonNull
import androidx.lifecycle.*
import kotlinx.coroutines.launch

class ImageConvertViewModel(application: Application,
                            private val imageToCodeConverter: ImageToCodeConverter) : AndroidViewModel(application) {

    private val liveData: MutableLiveData<ConvertResponse> = MutableLiveData()

    fun observe(@NonNull owner: LifecycleOwner, @NonNull observer: Observer<ConvertResponse>) {
        liveData.observe(owner, observer)
    }

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
