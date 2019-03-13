package alytvyniuk.com.barcodewidget

import alytvyniuk.com.barcodewidget.EditActivity.Companion.REQUEST_EDIT_ACTIVITY
import alytvyniuk.com.barcodewidget.converters.CodeToImageConverter
import alytvyniuk.com.barcodewidget.model.Barcode
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_preview.*
import javax.inject.Inject

class PreviewActivity : AppCompatActivity() {

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
        val barcode = intent.getBarcodeEntity()
        updateView(barcode)
        editButton.setOnClickListener {
            openEditActivity(barcode)
        }
    }

    private fun updateView(barcode: Barcode) {
        val bitmap = codeToImageConverter.convert(barcode.rawBarcode)
        barcodeImageView.setImageBitmap(bitmap)
        dataTextView.text = barcode.rawBarcode.value
        titleTextView.text = barcode.title
        colorFrameView.setBackgroundColor(barcode.color ?: Color.TRANSPARENT)
    }

    private fun openEditActivity(barcode: Barcode) {
        val intent = EditActivity.intent(this, barcode)
        startActivityForResult(intent, REQUEST_EDIT_ACTIVITY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_EDIT_ACTIVITY) {
            val barcode = data?.getBarcodeEntity()
            if (barcode != null) {
                updateView(barcode)
            } else {
                throw IllegalArgumentException("No rawBarcode found in extra")
            }
        }
    }
}