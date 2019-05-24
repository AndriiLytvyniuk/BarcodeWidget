package alytvyniuk.com.barcodewidget

import alytvyniuk.com.barcodewidget.converters.CodeToImageConverter
import alytvyniuk.com.barcodewidget.db.BarcodeDao
import alytvyniuk.com.barcodewidget.model.Barcode
import alytvyniuk.com.barcodewidget.model.isValidWidgetId
import alytvyniuk.com.barcodewidget.utils.CoroutineScopeActivity
import alytvyniuk.com.barcodewidget.utils.getBarcodeActivityIntent
import alytvyniuk.com.barcodewidget.utils.getWidgetId
import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_barcode_list.*
import javax.inject.Inject

private const val TAG = "ListActivity"
private const val REQUEST_UPDATE_WIDGET = 1
private const val REQUEST_SHOW = 2

class ListActivity : CoroutineScopeActivity(), OnItemClickListener {

    companion object {
        fun intent(context: Context, widgetId: Int = AppWidgetManager.INVALID_APPWIDGET_ID): Intent {
            return context.getBarcodeActivityIntent(ListActivity::class.java, widgetId = widgetId)
        }
    }

    @Inject
    lateinit var barcodeDao: BarcodeDao
    @Inject
    lateinit var codeToImageConverter: CodeToImageConverter
    private lateinit var adapter: BarcodeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_barcode_list)
        setSupportActionBar(toolbar)
        App.component().inject(this)
        barcodeListView.layoutManager = LinearLayoutManager(this)
        barcodeListView.addItemDecoration(
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        )
        adapter = BarcodeAdapter(codeToImageConverter, this)
        barcodeListView.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        updateUI()
    }

    override fun onItemClicked(barcode: Barcode) {
        val widgetId: Int = intent.getWidgetId()
        val nextIntent = EditActivity.intent(this, barcode, widgetId)
        if (widgetId.isValidWidgetId()) {
            if (barcode.widgetId.isValidWidgetId()) {
                Toast.makeText(this, R.string.barcode_is_reserved, Toast.LENGTH_LONG).show()
            } else {
                startActivityForResult(nextIntent, REQUEST_UPDATE_WIDGET)
            }
        } else {
            startActivityForResult(nextIntent, REQUEST_SHOW)
        }
    }

    private fun updateUI() {
        noBarcodesTextView.visibility = View.GONE
        barcodeListView.visibility = View.GONE
        launchWithResult({
            barcodeDao.loadAll()
        }, { barcodes ->
            barcodeListView.visibility = View.VISIBLE
            adapter.setBarcodes(barcodes)
            adapter.setOnItemClickListener(this@ListActivity)
            adapter.notifyDataSetChanged()
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_UPDATE_WIDGET -> {
                    setResult(Activity.RESULT_OK)
                    finish()
                }
            }
        }
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
