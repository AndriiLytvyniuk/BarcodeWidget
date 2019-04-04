package alytvyniuk.com.barcodewidget.utils

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

class ReusableCompositeDisposable {

    private var compositeDisposable: CompositeDisposable? = null

    fun add(disposable: Disposable) {
        if (compositeDisposable == null) {
            compositeDisposable = CompositeDisposable()
        }
        compositeDisposable?.add(disposable)
    }

    fun dispose() {
        compositeDisposable?.dispose()
        compositeDisposable = null
    }
}