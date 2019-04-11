package alytvyniuk.com.barcodewidget

import alytvyniuk.com.barcodewidget.converters.CodeToImageConverter
import alytvyniuk.com.barcodewidget.db.BarcodeDao
import alytvyniuk.com.barcodewidget.model.Barcode
import alytvyniuk.com.barcodewidget.model.RawBarcode
import alytvyniuk.com.barcodewidget.model.isValidWidgetId
import alytvyniuk.com.barcodewidget.utils.DisposeActivity
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
import android.widget.ImageView
import androidx.annotation.VisibleForTesting
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_edit.*
import javax.inject.Inject
import kotlin.random.Random

private const val TAG = "EditActivity"
private const val KEY_WIDGET_ID = "KEY_WIDGET_ID"
private const val KEY_BARCODE = "KEY_BARCODE"
private const val COLORS_NUMBER = 12

class EditActivity : DisposeActivity(), View.OnClickListener {

    companion object {
        const val REQUEST_EDIT_ACTIVITY = 4

        /**
         * If @param barcode and widgetId are valid, means edit.
         * If barcode is valid, and widgetId is not, means create new and saveAndExit
         */
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
        initialBarcode = intent.getBarcode()
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
        val disposable = barcodeImage.setImageFromBarcode(codeToImageConverter, barcode.rawBarcode)
        addDisposable(disposable)
        dataTextView.text = barcode.rawBarcode.value
        formatTextView.text = barcode.rawBarcode.format.toString()
        titleEditText.setText(initialBarcode.title)
        colorFrameView.setBackgroundColor(color)
    }

    private fun saveAndExit(barcode: Barcode) {
        val id = initialBarcode.id
        val observable = if (id == BarcodeDao.INVALID_DB_ID) {
            Log.d(TAG, "Save new barcode: $barcode")
            barcodeDao.insert(barcode)
        } else {
            Log.d(TAG, "Update barcode: $barcode")
            barcodeDao.update(barcode)
        }
        addDisposable(observable.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                exitAfterSaved(barcode)
            }
        )
    }

    private fun exitAfterSaved(barcode: Barcode) {
        if (newWidgetId.isValidWidgetId()) {
            updateWidgetProvider(newWidgetId)
        }
        val result = Intent().putExtra(KEY_BARCODE, barcode)
        setResult(Activity.RESULT_OK, result)
        finish()
    }

    private fun updateWidgetProvider(widgetId : Int) {
        sendBroadcast(Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE)
            .putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, intArrayOf(widgetId)))
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.confirmButton -> {
                val barcode = initialBarcode
                barcode.title = titleEditText.text.toString()
                barcode.widgetId = newWidgetId
                barcode.color = color
                saveAndExit(barcode)
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
        if (newWidgetId.isValidWidgetId() || initialBarcode.id == BarcodeDao.INVALID_DB_ID) {
            menu.findItem(R.id.delete).isVisible = false
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.delete -> {
                barcodeDao.delete(initialBarcode)
                    .subscribeOn(Schedulers.io())
                    .subscribe {
                        finish()
                    }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun setSupportActionBar(toolbar: Toolbar?) {
        super.setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}

object BarcodeActivityHelper {

    fun <T> intent(clazz: Class<T>, context: Context, barcode: Barcode? = null,
                   widgetId: Int = AppWidgetManager.INVALID_APPWIDGET_ID): Intent {
        val intent = Intent(context, clazz)
        return appendExtras(intent, barcode, widgetId)
    }

    @VisibleForTesting
    fun appendExtras(intent: Intent, barcode: Barcode? = null,
                     widgetId: Int = AppWidgetManager.INVALID_APPWIDGET_ID) : Intent {
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

fun Intent.getBarcode() : Barcode = getParcelableExtra(KEY_BARCODE)

fun ImageView.setImageFromBarcode(codeToImageConverter: CodeToImageConverter, rawBarcode : RawBarcode) : Disposable {
    visibility = View.INVISIBLE
    return codeToImageConverter.convert(rawBarcode)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe { bitmap ->
            setImageBitmap(bitmap)
            visibility = View.VISIBLE
        }
}


