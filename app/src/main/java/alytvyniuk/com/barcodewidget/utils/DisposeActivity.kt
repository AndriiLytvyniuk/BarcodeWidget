package alytvyniuk.com.barcodewidget.utils

import androidx.appcompat.app.AppCompatActivity
import io.reactivex.disposables.Disposable

abstract class DisposeActivity : AppCompatActivity() {

    private val reusableDisposable = ReusableCompositeDisposable()

    protected fun getReusableCompositeDisposable() = reusableDisposable

    protected fun addDisposable(disposable: Disposable) {
        reusableDisposable.add(disposable)
    }

    override fun onStop() {
        super.onStop()
        reusableDisposable.dispose()
    }
}
