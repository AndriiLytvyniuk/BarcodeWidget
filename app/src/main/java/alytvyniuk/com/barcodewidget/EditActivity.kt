package alytvyniuk.com.barcodewidget

import alytvyniuk.com.barcodewidget.converters.CodeToImageConverter
import alytvyniuk.com.barcodewidget.db.BarcodeDao
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

private const val KEY_WIDGET_ID = "KEY_WIDGET_ID"
private const val KEY_BARCODE = "KEY_BARCODE"

class EditActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        const val REQUEST_EDIT_ACTIVITY = 4

        fun intent(context: Context,
                   barcodeEntity: BarcodeEntity,
                   widgetId: Int = AppWidgetManager.INVALID_APPWIDGET_ID): Intent {
            return BarcodeActivityHelper.intent(EditActivity::class.java, context, barcodeEntity, widgetId)
        }
    }

    @Inject lateinit var barcodeDao: BarcodeDao
    @Inject lateinit var codeToImageConverter: CodeToImageConverter
    private var newWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID
    private lateinit var initialBarcode: BarcodeEntity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        setSupportActionBar(toolbar)
        App.component().inject(this)
        newWidgetId = intent.getWidgetId()
        initialBarcode = intent.getBarcodeEntity()
        initUI(initialBarcode)
        confirmButton.setOnClickListener(this)
    }

    private fun initUI(barcodeEntity: BarcodeEntity) {
        val bitmap = codeToImageConverter.convert(barcodeEntity.barcode)
        barcodeImage.setImageBitmap(bitmap)
        dataTextView.text = barcodeEntity.barcode.value
        formatTextView.text = barcodeEntity.barcode.format.toString()
        notesEditText.setText(initialBarcode.note)
    }

    private fun save(barcodeEntity: BarcodeEntity) {
        val id = initialBarcode.id
        if (id == BarcodeEntity.INVALID_DB_ID) {
            barcodeDao.insert(barcodeEntity)
        } else {
            barcodeDao.update(barcodeEntity)
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
                val barcodeEntity = initialBarcode
                barcodeEntity.note = notesEditText.text.toString()
                barcodeEntity.widgetId = newWidgetId
                save(barcodeEntity)
                if (newWidgetId.isValidWidgetId()) {
                    updateWidgetProvider(newWidgetId)
                }
                val result = Intent().putExtra(KEY_BARCODE, barcodeEntity)
                setResult(Activity.RESULT_OK, result)
                finish()
            }
        }
    }
}

object BarcodeActivityHelper {

    fun <T> intent(clazz: Class<T>, context: Context, barcode: BarcodeEntity? = null,
        widgetId: Int = AppWidgetManager.INVALID_APPWIDGET_ID): Intent {
        val intent = Intent(context, clazz)
        if (widgetId.isValidWidgetId()) {
            intent.putExtra(KEY_WIDGET_ID, widgetId)
        }
        if (barcode != null) {
            intent.putExtra(KEY_BARCODE, barcode)
        }
        return intent
    }
}

fun Intent.getWidgetId() = getIntExtra(KEY_WIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID)

fun Intent.getBarcodeEntity() : BarcodeEntity = getParcelableExtra(KEY_BARCODE)


