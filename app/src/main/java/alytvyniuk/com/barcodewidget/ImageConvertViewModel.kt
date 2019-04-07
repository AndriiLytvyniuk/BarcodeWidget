package alytvyniuk.com.barcodewidget

import alytvyniuk.com.barcodewidget.converters.ImageToCodeConverter
import alytvyniuk.com.barcodewidget.model.RawBarcode
import alytvyniuk.com.barcodewidget.utils.ReusableCompositeDisposable
import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import androidx.annotation.NonNull
import androidx.lifecycle.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ImageConvertViewModel(application: Application,
                            private val imageToCodeConverter: ImageToCodeConverter) : AndroidViewModel(application) {

    private val liveData: MutableLiveData<ConvertResponse> = MutableLiveData()
    private val reusableDisposable = ReusableCompositeDisposable()

    fun observe(@NonNull owner: LifecycleOwner, @NonNull observer: Observer<ConvertResponse>) {
        liveData.observe(owner, observer)
    }

    fun performConversion(uri: Uri) {
        executeConversion(Observable.fromCallable {
            getApplication<Application>().getBitmapForImageUri(uri)
        }.flatMap { bitmap -> imageToCodeConverter.convert(bitmap) })
    }

    fun performConversion(bitmap: Bitmap) {
        executeConversion(imageToCodeConverter.convert(bitmap))
    }

    private fun executeConversion(observable: Observable<RawBarcode>) {
        val disposable = observable
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

class ImageConvertModelFactory(private val application: Application, private val imageToCodeConverter: ImageToCodeConverter) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ImageConvertViewModel(application, imageToCodeConverter) as T
    }
}

data class ConvertResponse(val barcode: RawBarcode? = null, val exception: Throwable? = null)
