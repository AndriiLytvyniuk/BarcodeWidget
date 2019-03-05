package alytvyniuk.com.barcodewidget

import alytvyniuk.com.barcodewidget.EditActivity.Companion.REQUEST_EDIT_ACTIVITY
import alytvyniuk.com.barcodewidget.converters.CodeToImageConverter
import alytvyniuk.com.barcodewidget.model.Barcode
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_preview.*
import java.lang.IllegalArgumentException
import javax.inject.Inject

class PreviewActivity : AppCompatActivity() {

    companion object {
        fun intent(context: Context, barcode: Barcode, widgetId: Int): Intent {
            return BarcodeActivityHelper.intent(PreviewActivity::class.java, context, barcode, widgetId)
        }
    }

    @Inject lateinit var codeToImageConverter: CodeToImageConverter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preview)
        App.component().inject(this)
        val barcode = intent.getParcelableExtra<Barcode>(BarcodeActivityHelper.KEY_BARCODE)
        updateView(barcode)
        val widgetId = intent.getIntExtra(BarcodeActivityHelper.KEY_WIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID)
        editButton.setOnClickListener {
            openEditActivity(widgetId, barcode)
        }
    }

    private fun updateView(barcode: Barcode) {
        val bitmap = codeToImageConverter.convert(barcode)
        barcodeImageView.setImageBitmap(bitmap)
        dataTextView.text = barcode.value
    }

    private fun openEditActivity(widgetId: Int, barcode: Barcode) {
        val intent = EditActivity.intent(this, barcode, widgetId)
        startActivityForResult(intent, REQUEST_EDIT_ACTIVITY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_EDIT_ACTIVITY) {
            val barcode = data?.getParcelableExtra<Barcode>(BarcodeActivityHelper.KEY_BARCODE)
            if (barcode != null) {
                updateView(barcode)
            } else {
                throw IllegalArgumentException("No barcode found in extra")
            }
        }
    }
}