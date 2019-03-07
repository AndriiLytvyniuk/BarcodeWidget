package alytvyniuk.com.barcodewidget

import alytvyniuk.com.barcodewidget.db.BarcodeDao
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_barcode_list.*
import javax.inject.Inject

class BarcodeListActivity : AppCompatActivity() {

    companion object {
        fun intent(context: Context) : Intent {
            return Intent(context, BarcodeListActivity::class.java)
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
        val barcodes = barcodeDao.loadAll()
        val adapter = BarcodeAdapter()
        barcodeListView.adapter = adapter
        adapter.setBarcodes(barcodes)
    }
}