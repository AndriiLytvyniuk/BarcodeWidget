package alytvyniuk.com.barcodewidget

import alytvyniuk.com.barcodewidget.EditActivity.Companion.REQUEST_EDIT_ACTIVITY
import alytvyniuk.com.barcodewidget.converters.CodeToImageConverter
import alytvyniuk.com.barcodewidget.model.Barcode
import alytvyniuk.com.barcodewidget.utils.DisposeActivity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_preview.*
import javax.inject.Inject

private const val TAG = "PreviewActivity"

class PreviewActivity : DisposeActivity() {

    companion object {
        fun intent(context: Context, barcode: Barcode): Intent {
            return BarcodeActivityHelper.intent(PreviewActivity::class.java, context, barcode)
        }
    }

    @Inject lateinit var codeToImageConverter: CodeToImageConverter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preview)
        App.component().inject(this)
        val barcode = intent.getBarcode()
        updateView(barcode)
    }

    private fun updateView(barcode: Barcode) {
        Log.d(TAG, "updateView: $barcode")
        val disposable = barcodeImage.setImageFromBarcode(codeToImageConverter, barcode.rawBarcode)
        addDisposable(disposable)
        dataTextView.text = barcode.rawBarcode.value
        titleTextView.text = barcode.title
        colorFrameView.setBackgroundColor(barcode.color ?: Color.TRANSPARENT)

        val onClickListener = View.OnClickListener { v ->
            when (v.id) {
                R.id.editButton -> openEditActivity(barcode)
                R.id.transparentFrame -> onBackPressed()
            }
        }
        editButton.setOnClickListener(onClickListener)
        transparentFrame.setOnClickListener(onClickListener)
        // Prevent close activity on click inside visible area
        backgroundView.setOnClickListener(onClickListener)
    }

    private fun openEditActivity(barcode: Barcode) {
        val intent = EditActivity.intent(this, barcode)
        startActivityForResult(intent, REQUEST_EDIT_ACTIVITY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_EDIT_ACTIVITY) {
            val barcode = data?.getBarcode()
            if (barcode != null) {
                updateView(barcode)
            }
        }
    }
}
