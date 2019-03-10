package alytvyniuk.com.barcodewidget

import alytvyniuk.com.barcodewidget.BarcodeActivityHelper.KEY_BARCODE
import alytvyniuk.com.barcodewidget.BarcodeActivityHelper.KEY_WIDGET_ID
import alytvyniuk.com.barcodewidget.BarcodeActivityHelper.KEY_DB_ID
import alytvyniuk.com.barcodewidget.converters.CodeToImageConverter
import alytvyniuk.com.barcodewidget.db.BarcodeDao
import alytvyniuk.com.barcodewidget.model.Barcode
import alytvyniuk.com.barcodewidget.model.BarcodeEntity
import alytvyniuk.com.barcodewidget.model.isValidWidgetId
import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_edit.*
import javax.inject.Inject

class EditActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        const val REQUEST_EDIT_ACTIVITY = 4

        fun intent(context: Context,
                   barcode: Barcode,
                   widgetId: Int = AppWidgetManager.INVALID_APPWIDGET_ID,
                   dbId: Int = BarcodeEntity.INVALID_DB_ID): Intent {
            return BarcodeActivityHelper.intent(EditActivity::class.java, context, barcode, widgetId, dbId)
        }
    }

    @Inject lateinit var barcodeDao: BarcodeDao
    @Inject lateinit var codeToImageConverter: CodeToImageConverter
    private var widgetId = AppWidgetManager.INVALID_APPWIDGET_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        setSupportActionBar(toolbar)
        App.component().inject(this)
        widgetId = intent.getWidgetId()
        updateUI()
        confirmButton.setOnClickListener(this)
    }

    private fun updateUI() {
        val barcode = intent.getParcelableExtra<Barcode>(KEY_BARCODE)
        val bitmap = codeToImageConverter.convert(barcode)
        barcodeImage.setImageBitmap(bitmap)
    }

    private fun save(barcode: Barcode, widgetId : Int) {
        val id = intent.getDbId()
        val entity = BarcodeEntity(barcode, widgetId, id)
        if (id == BarcodeEntity.INVALID_DB_ID) {
            barcodeDao.insert(entity)
        } else {
            barcodeDao.update(entity)
        }
    }

    private fun updateWidgetProvider(widgetId : Int) {
        sendBroadcast(Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE)
            .putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, intArrayOf(widgetId)))
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.confirmButton -> {
                //TODO take barcode value from UI
                val barcode = intent.getParcelableExtra<Barcode>(KEY_BARCODE)
                save(barcode, widgetId)
                if (widgetId.isValidWidgetId()) {
                    updateWidgetProvider(widgetId)
                }
                val result = Intent().putExtra(KEY_BARCODE, barcode).putExtra(KEY_WIDGET_ID, widgetId)
                setResult(Activity.RESULT_OK, result)
                finish()
            }
        }
    }
}

object BarcodeActivityHelper {
    const val KEY_WIDGET_ID = "KEY_WIDGET_ID"
    const val KEY_BARCODE = "KEY_BARCODE"
    const val KEY_DB_ID = "KEY_DB_ID"

    fun <T> intent(clazz: Class<T>, context: Context, barcode: Barcode? = null,
        widgetId: Int = AppWidgetManager.INVALID_APPWIDGET_ID, dbId : Int = BarcodeEntity.INVALID_DB_ID): Intent {
        val intent = Intent(context, clazz).putExtra(KEY_WIDGET_ID, widgetId)
        if (barcode != null) {
            intent.putExtra(KEY_BARCODE, barcode)
        }
        if (dbId != BarcodeEntity.INVALID_DB_ID) {
            intent.putExtra(KEY_DB_ID, dbId)
        }
        return intent
    }
}

fun Intent.getWidgetId() = getIntExtra(KEY_WIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID)

fun Intent.getDbId() = getIntExtra(KEY_DB_ID, BarcodeEntity.INVALID_DB_ID)


