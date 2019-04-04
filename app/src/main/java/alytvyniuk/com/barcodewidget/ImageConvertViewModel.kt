package alytvyniuk.com.barcodewidget

import alytvyniuk.com.barcodewidget.converters.ImageToCodeConverter
import alytvyniuk.com.barcodewidget.model.RawBarcode
import android.graphics.Bitmap
import androidx.annotation.NonNull
import androidx.lifecycle.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class ImageConvertViewModel(private val imageToCodeConverter: ImageToCodeConverter) : ViewModel() {

    private val liveData: MutableLiveData<ConvertResponse> = MutableLiveData()
    private val compositeDisposable = CompositeDisposable()

    fun observe(@NonNull owner: LifecycleOwner, @NonNull observer: Observer<ConvertResponse>) {
        liveData.observe(owner, observer)
    }

    fun performConversion(bitmap: Bitmap) {
        val disposable = imageToCodeConverter.convert(bitmap)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableObserver<RawBarcode>() {
                override fun onComplete() {
                }

                override fun onNext(rawBarcode: RawBarcode) {
                    liveData.value = ConvertResponse(rawBarcode)
                }

                override fun onError(t: Throwable) {
                    liveData.value = ConvertResponse(exception = t)
                }

            })
        compositeDisposable.addAll(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}

class ImageConvertModelFactory(val imageToCodeConverter: ImageToCodeConverter): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ImageConvertViewModel(imageToCodeConverter) as T
    }
}

data class ConvertResponse(val barcode: RawBarcode? = null, val exception: Throwable? = null)