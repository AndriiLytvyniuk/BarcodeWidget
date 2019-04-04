package alytvyniuk.com.barcodewidget

import alytvyniuk.com.barcodewidget.converters.ImageToCodeConverter
import alytvyniuk.com.barcodewidget.model.RawBarcode
import alytvyniuk.com.barcodewidget.utils.ReusableCompositeDisposable
import android.graphics.Bitmap
import androidx.annotation.NonNull
import androidx.lifecycle.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ImageConvertViewModel(private val imageToCodeConverter: ImageToCodeConverter) : ViewModel() {

    private val liveData: MutableLiveData<ConvertResponse> = MutableLiveData()
    private val reusableDisposable = ReusableCompositeDisposable()

    fun observe(@NonNull owner: LifecycleOwner, @NonNull observer: Observer<ConvertResponse>) {
        liveData.observe(owner, observer)
    }

    fun performConversion(bitmap: Bitmap) {
        val disposable = imageToCodeConverter.convert(bitmap)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ rawBarcode ->
                liveData.value = ConvertResponse(rawBarcode)
            }, { t ->
                liveData.value = ConvertResponse(exception = t)
            })
        reusableDisposable.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        reusableDisposable.dispose()
    }
}

class ImageConvertModelFactory(private val imageToCodeConverter: ImageToCodeConverter) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ImageConvertViewModel(imageToCodeConverter) as T
    }
}

data class ConvertResponse(val barcode: RawBarcode? = null, val exception: Throwable? = null)