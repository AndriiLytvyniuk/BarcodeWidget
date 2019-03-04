package alytvyniuk.com.barcodewidget

import alytvyniuk.com.barcodewidget.converters.CodeToImageConverter
import alytvyniuk.com.barcodewidget.db.BarcodeDao
import alytvyniuk.com.barcodewidget.model.Barcode
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_edit.*
import javax.inject.Inject

private const val KEY_WIDGET_ID = "KEY_WIDGET_ID"
private const val KEY_BARCODE = "KEY_BARCODE"

class EditActivity : AppCompatActivity() {

    companion object {
        fun intent(context: Context, widgetId: Int, barcode: Barcode): Intent {
            return Intent(context, EditActivity::class.java)
                .putExtra(KEY_WIDGET_ID, widgetId)
                .putExtra(KEY_BARCODE, barcode)
        }
    }

    @Inject lateinit var codeToImageConverter: CodeToImageConverter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        setSupportActionBar(toolbar)
        App.component().inject(this)
        val barcode = intent.getParcelableExtra<Barcode>(KEY_BARCODE)
        val bitmap = codeToImageConverter.convert(barcode)
        barcodeImage.setImageBitmap(bitmap)
    }
}