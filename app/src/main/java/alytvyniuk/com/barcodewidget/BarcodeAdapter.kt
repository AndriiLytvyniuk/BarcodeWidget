package alytvyniuk.com.barcodewidget

import alytvyniuk.com.barcodewidget.converters.CodeToImageConverter
import alytvyniuk.com.barcodewidget.model.Barcode
import alytvyniuk.com.barcodewidget.model.isValidWidgetId
import android.graphics.Color
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.barcode_list_item.view.*

class BarcodeAdapter(private val codeToImageConverter: CodeToImageConverter)
    : RecyclerView.Adapter<BarcodeAdapter.BarcodeItemHolder>() {

    private var barcodes : List<Barcode> = listOf()
    private var onItemClickListener : OnItemClickListener? = null

    fun setBarcodes(list : List<Barcode>) {
        barcodes = list
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int)
            = BarcodeItemHolder(parent.inflateWithoutAttach(R.layout.barcode_list_item))

    override fun getItemCount() = barcodes.size

    override fun onBindViewHolder(holder: BarcodeItemHolder, index: Int) = holder.bind(barcodes[index])

    private fun ViewGroup.inflateWithoutAttach(layoutRes: Int): View {
        return LayoutInflater.from(context).inflate(layoutRes, this, false)
    }

    inner class BarcodeItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        fun bind(item: Barcode) {
            itemView.setOnClickListener(this)
            itemView.dataTextView.text = item.rawBarcode.value
            val bitmap = codeToImageConverter.convert(item.rawBarcode)
            itemView.barcodeImageView.setImageBitmap(bitmap)
            if (TextUtils.isEmpty(item.title)) {
                itemView.titleTextView.visibility = View.GONE
            } else {
                itemView.titleTextView.visibility = View.VISIBLE
                itemView.titleTextView.text = item.title
            }
            itemView.availabilityIndicatorView.visibility =
                if (item.widgetId.isValidWidgetId()) View.VISIBLE else View.GONE
        }

        override fun onClick(v: View?) {
            onItemClickListener?.onItemClicked(barcodes[adapterPosition])
        }
    }
}

interface OnItemClickListener {
    fun onItemClicked(barcode: Barcode)
}