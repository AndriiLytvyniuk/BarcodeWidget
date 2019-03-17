package alytvyniuk.com.barcodewidget

import alytvyniuk.com.barcodewidget.converters.CodeToImageConverter
import alytvyniuk.com.barcodewidget.db.BarcodeDao
import alytvyniuk.com.barcodewidget.model.Barcode
import alytvyniuk.com.barcodewidget.model.isValidWidgetId
import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_edit.*
import java.lang.IllegalStateException
import javax.inject.Inject
import kotlin.random.Random

private const val TAG = "EditActivity"
private const val KEY_WIDGET_ID = "KEY_WIDGET_ID"
private const val KEY_BARCODE = "KEY_BARCODE"
private const val COLORS_NUMBER = 12

class EditActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        const val REQUEST_EDIT_ACTIVITY = 4

        fun intent(context: Context,
                   barcode: Barcode,
                   widgetId: Int = AppWidgetManager.INVALID_APPWIDGET_ID): Intent {
            return BarcodeActivityHelper.intent(EditActivity::class.java, context, barcode, widgetId)
        }
    }

    @Inject lateinit var barcodeDao: BarcodeDao
    @Inject lateinit var codeToImageConverter: CodeToImageConverter
    private var newWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID
    private lateinit var initialBarcode: Barcode
    private var color : Int = Color.TRANSPARENT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        setSupportActionBar(toolbar)
        App.component().inject(this)
        initialBarcode = intent.getBarcodeEntity()
        initWidgetId()
        color = initialBarcode.color ?: getRandomColor()
        initUI(initialBarcode)
        confirmButton.setOnClickListener(this)
    }

    private fun initWidgetId() {
        newWidgetId = intent.getWidgetId()
        if (initialBarcode.widgetId.isValidWidgetId()) {
            if (newWidgetId.isValidWidgetId()) {
                throw IllegalStateException("Can't reuse record of existing barcode. " +
                        "From intent=$newWidgetId, from barcode=${initialBarcode.widgetId}")
            } else {
                newWidgetId = initialBarcode.widgetId
            }
        }
    }

    private fun initUI(barcode: Barcode) {
        val bitmap = codeToImageConverter.convert(barcode.rawBarcode)
        barcodeImage.setImageBitmap(bitmap)
        dataTextView.text = barcode.rawBarcode.value
        formatTextView.text = barcode.rawBarcode.format.toString()
        notesEditText.setText(initialBarcode.title)
        colorFrameView.setBackgroundColor(color)
    }

    private fun save(barcode: Barcode) {
        val id = initialBarcode.id
        if (id == BarcodeDao.INVALID_DB_ID) {
            Log.d(TAG, "Save new barcode: $barcode")
            barcodeDao.insert(barcode)
        } else {
            Log.d(TAG, "Update barcode: $barcode")
            barcodeDao.update(barcode)
        }
    }

    private fun updateWidgetProvider(widgetId : Int) {
        sendBroadcast(Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE)
            .putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, intArrayOf(widgetId)))
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.confirmButton -> {
                //TODO take rawBarcode value from UI
                val barcode = initialBarcode
                barcode.title = notesEditText.text.toString()
                barcode.widgetId = newWidgetId
                barcode.color = color
                save(barcode)
                if (newWidgetId.isValidWidgetId()) {
                    updateWidgetProvider(newWidgetId)
                }
                val result = Intent().putExtra(KEY_BARCODE, barcode)
                setResult(Activity.RESULT_OK, result)
                finish()
            }
        }
    }

    private fun getRandomColor() : Int {
        val number = Random.nextInt(COLORS_NUMBER)
        val colorId = resources.getIdentifier("choice_color_$number", "color", packageName)
        return ContextCompat.getColor(this, colorId)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_delete, menu)
        if (newWidgetId.isValidWidgetId()) {
            menu.findItem(R.id.delete).isVisible = false
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.delete -> {
                barcodeDao.delete(initialBarcode)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}

object BarcodeActivityHelper {

    fun <T> intent(clazz: Class<T>, context: Context, barcode: Barcode? = null,
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

fun Intent.getBarcodeEntity() : Barcode = getParcelableExtra(KEY_BARCODE)


