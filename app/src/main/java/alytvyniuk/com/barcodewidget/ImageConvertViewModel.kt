package alytvyniuk.com.barcodewidget

import alytvyniuk.com.barcodewidget.converters.ImageToCodeConverter
import alytvyniuk.com.barcodewidget.model.RawBarcode
import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ImageConvertViewModel(@Inject private val imageToCodeConverter: ImageToCodeConverter) : ViewModel() {

    private val liveData: MutableLiveData<ConvertResponse> = MutableLiveData()
    private val compositeDisposable = CompositeDisposable()

    fun performConversion(bitmap: Bitmap) {
        val disposable = imageToCodeConverter.convert(bitmap)
            .subscribeOn(Schedulers.io())
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

data class ConvertResponse(val barcode: RawBarcode? = null, val exception: Throwable? = null)