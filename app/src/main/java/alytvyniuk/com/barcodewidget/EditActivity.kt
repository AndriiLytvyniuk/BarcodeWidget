package alytvyniuk.com.barcodewidget

import alytvyniuk.com.barcodewidget.BarcodeActivityHelper.KEY_BARCODE
import alytvyniuk.com.barcodewidget.BarcodeActivityHelper.KEY_WIDGET_ID
import alytvyniuk.com.barcodewidget.converters.CodeToImageConverter
import alytvyniuk.com.barcodewidget.db.BarcodeDao
import alytvyniuk.com.barcodewidget.model.Barcode
import alytvyniuk.com.barcodewidget.model.BarcodeEntity
import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_edit.*
import javax.inject.Inject

class EditActivity : AppCompatActivity() {

    companion object {
        const val REQUEST_EDIT_ACTIVITY = 3

        fun intent(context: Context, barcode: Barcode, widgetId: Int): Intent {
            return BarcodeActivityHelper.intent(EditActivity::class.java, context, barcode, widgetId)
        }
    }

    @Inject lateinit var barcodeDao: BarcodeDao
    @Inject lateinit var codeToImageConverter: CodeToImageConverter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        setSupportActionBar(toolbar)
        App.component().inject(this)
        val barcode = intent.getParcelableExtra<Barcode>(KEY_BARCODE)
        val bitmap = codeToImageConverter.convert(barcode)
        val widgetId : Int = intent.getIntExtra(KEY_WIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID)
        barcodeImage.setImageBitmap(bitmap)
        confirmButton.setOnClickListener {
            save(barcode, widgetId)
            updateWidgetProvider(widgetId)
            val result = Intent().putExtra(KEY_BARCODE, barcode).putExtra(KEY_WIDGET_ID, widgetId)
            setResult(Activity.RESULT_OK, result)
            finish()
        }
    }

    private fun save(barcode: Barcode, widgetId : Int) {
        barcodeDao.insert(BarcodeEntity(barcode, widgetId))
    }

    private fun updateWidgetProvider(widgetId : Int) {
        sendBroadcast(Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE)
            .putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, intArrayOf(widgetId)))
    }
}

object BarcodeActivityHelper {
    const val KEY_WIDGET_ID = "KEY_WIDGET_ID"
    const val KEY_BARCODE = "KEY_BARCODE"

    fun <T> intent(
        clazz: Class<T>,
        context: Context,
        barcode: Barcode,
        widgetId: Int = AppWidgetManager.INVALID_APPWIDGET_ID
    ) = Intent(context, clazz).putExtra(KEY_BARCODE, barcode).putExtra(KEY_WIDGET_ID, widgetId)
}


