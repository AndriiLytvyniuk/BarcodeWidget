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
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.ColorDrawable
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
import kotlinx.android.synthetic.main.edit_color_picker.*
import kotlinx.android.synthetic.main.edit_data_layout.*
import javax.inject.Inject
import kotlin.random.Random

private const val TAG = "EditActivity"
private const val KEY_WIDGET_ID = "KEY_WIDGET_ID"
private const val KEY_BARCODE = "KEY_BARCODE"
private const val KEY_TITLE = "KEY_TITLE"
private const val KEY_COLOR = "KEY_COLOR"
private const val COLORS_NUMBER = 8

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
    private lateinit var barcode: Barcode
    private val colorPicker = ButtonColorPicker()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        setSupportActionBar(toolbar)
        App.component().inject(this)
        barcode = intent.getBarcode()
        savedInstanceState?.let {
            barcode.title = savedInstanceState.getString(KEY_TITLE)
            barcode.color = savedInstanceState.getInt(KEY_COLOR)
        }
        initWidgetId()
        barcode.color = barcode.color ?: getRandomColor()
        updateUI(barcode)
    }

    private fun initWidgetId() {
        newWidgetId = intent.getWidgetId()
        if (barcode.widgetId.isValidWidgetId()) {
            if (newWidgetId.isValidWidgetId()) {
                throw IllegalStateException("Can't reuse record of existing barcode. " +
                        "From intent=$newWidgetId, from barcode=${barcode.widgetId}")
            } else {
                newWidgetId = barcode.widgetId
            }
        }
    }

    private fun updateUI(barcode: Barcode) {
        val disposable = barcodeImage.setImageFromBarcode(codeToImageConverter, barcode.rawBarcode)
        addDisposable(disposable)
        initColorPicker(barcode.color!!)
        dataTextView.text = barcode.rawBarcode.value
        formatTextView.text = barcode.rawBarcode.format.toString()
        titleEditText.setText(this.barcode.title)
        changeColor(barcode.color!!)
        saveButton.setOnClickListener(this)
    }

    private fun initColorPicker(chosenColor : Int) {
        val colorsNumber = 8
        for (i in 0 until colorsNumber) {
            val id = resources.getIdentifier("colorView$i", "id", packageName)
            val imageView = findViewById<ImageView>(id)
            val colorId = resources.getIdentifier("choice_color_$i", "color", packageName)
            val color = ContextCompat.getColor(this, colorId)
            colorPicker.addView(imageView, color, chosenColor == color)
        }
        colorPicker.setOnColorListener(object : OnColorListener {
            override fun onColorSelected(color: Int) {
                changeColor(color)
            }
        })
    }

    private fun changeColor(color: Int) {
        val colorDrawable = ColorDrawable(color)
        colorFrameView.background = colorDrawable
        supportActionBar?.setBackgroundDrawable(colorDrawable)
    }

    private fun saveAndExit(barcode: Barcode) {
        val id = this.barcode.id
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
        Log.d(TAG, "updateWidgetProvider: $widgetId")
        sendBroadcast(Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE)
            .putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, intArrayOf(widgetId)))
    }

    private fun getRandomColor() : Int {
        val number = Random.nextInt(COLORS_NUMBER)
        val colorId = resources.getIdentifier("choice_color_$number", "color", packageName)
        return ContextCompat.getColor(this, colorId)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_edit, menu)
        if (newWidgetId.isValidWidgetId() || barcode.id == BarcodeDao.INVALID_DB_ID) {
            menu.findItem(R.id.delete).isVisible = false
        }
        return true
    }

    override fun onClick(v: View?) {
        barcode.title = titleEditText.text.toString()
        barcode.widgetId = newWidgetId
        barcode.color = colorPicker.getCurrentColor()
        saveAndExit(barcode)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.delete -> {
                barcodeDao.delete(barcode)
                    .subscribeOn(Schedulers.io())
                    .subscribe {
                        finish()
                    }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(KEY_TITLE, titleEditText.text.toString())
        outState.putInt(KEY_COLOR, colorPicker.getCurrentColor())
        super.onSaveInstanceState(outState)
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


