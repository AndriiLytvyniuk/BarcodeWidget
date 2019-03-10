package alytvyniuk.com.barcodewidget

import alytvyniuk.com.barcodewidget.db.BarcodeDao
import alytvyniuk.com.barcodewidget.model.BarcodeEntity
import alytvyniuk.com.barcodewidget.model.isValidWidgetId
import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_barcode_list.*
import javax.inject.Inject

private const val REQUEST_UPDATE_WIDGET = 1
private const val REQUEST_SHOW = 2

class BarcodeListActivity : AppCompatActivity(), OnItemClickListener {

    companion object {
        fun intent(context: Context, widgetId : Int = AppWidgetManager.INVALID_APPWIDGET_ID) : Intent {
            return BarcodeActivityHelper.intent(BarcodeListActivity::class.java, context, widgetId = widgetId)
        }
    }

    @Inject
    lateinit var barcodeDao: BarcodeDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_barcode_list)
        setSupportActionBar(toolbar)
        App.component().inject(this)
        barcodeListView.layoutManager = LinearLayoutManager(this)
        barcodeListView.addItemDecoration(
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        updateAdapter()
    }

    override fun onItemClicked(barcodeEntity: BarcodeEntity) {
        val widgetId : Int = intent.getWidgetId()
        val nextIntent = EditActivity.intent(this, barcodeEntity.barcode, barcodeEntity.widgetId)
        if (widgetId.isValidWidgetId()) {
            if (barcodeEntity.widgetId.isValidWidgetId()) {
                Toast.makeText(this, R.string.barcode_is_reserved, Toast.LENGTH_LONG).show()
            } else {
                startActivityForResult(nextIntent, REQUEST_UPDATE_WIDGET)
            }
        } else {
            startActivityForResult(nextIntent, REQUEST_SHOW)
        }
    }

    private fun updateAdapter() {
        val barcodes = barcodeDao.loadAll()
        val adapter = BarcodeAdapter()
        barcodeListView.adapter = adapter
        adapter.setBarcodes(barcodes)
        adapter.setOnItemClickListener(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_UPDATE_WIDGET -> {
                    setResult(Activity.RESULT_OK)
                    finish()
                }
                REQUEST_SHOW -> updateAdapter()
            }
        }
    }
}