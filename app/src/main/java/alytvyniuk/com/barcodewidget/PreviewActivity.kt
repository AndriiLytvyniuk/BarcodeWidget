package alytvyniuk.com.barcodewidget

import alytvyniuk.com.barcodewidget.EditActivity.Companion.REQUEST_EDIT_ACTIVITY
import alytvyniuk.com.barcodewidget.converters.CodeToImageConverter
import alytvyniuk.com.barcodewidget.model.BarcodeEntity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_preview.*
import javax.inject.Inject

class PreviewActivity : AppCompatActivity() {

    companion object {
        fun intent(context: Context, barcodeEntity: BarcodeEntity): Intent {
            return BarcodeActivityHelper.intent(PreviewActivity::class.java, context, barcodeEntity)
        }
    }

    @Inject lateinit var codeToImageConverter: CodeToImageConverter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preview)
        App.component().inject(this)
        val barcode = intent.getBarcodeEntity()
        updateView(barcode)
        editButton.setOnClickListener {
            openEditActivity(barcode)
        }
    }

    private fun updateView(barcodeEntity: BarcodeEntity) {
        val bitmap = codeToImageConverter.convert(barcodeEntity.barcode)
        barcodeImageView.setImageBitmap(bitmap)
        dataTextView.text = barcodeEntity.barcode.value
    }

    private fun openEditActivity(barcodeEntity: BarcodeEntity) {
        val intent = EditActivity.intent(this, barcodeEntity)
        startActivityForResult(intent, REQUEST_EDIT_ACTIVITY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_EDIT_ACTIVITY) {
            val barcode = data?.getBarcodeEntity()
            if (barcode != null) {
                updateView(barcode)
            } else {
                throw IllegalArgumentException("No barcode found in extra")
            }
        }
    }
}