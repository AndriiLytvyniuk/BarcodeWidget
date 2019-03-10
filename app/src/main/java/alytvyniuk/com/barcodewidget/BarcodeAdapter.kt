package alytvyniuk.com.barcodewidget

import alytvyniuk.com.barcodewidget.model.BarcodeEntity
import alytvyniuk.com.barcodewidget.model.isValidWidgetId
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.barcode_list_item.view.*

class BarcodeAdapter : RecyclerView.Adapter<BarcodeAdapter.BarcodeItemHolder>() {

    private var barcodes : List<BarcodeEntity> = listOf()
    private var onItemClickListener : OnItemClickListener? = null

    fun setBarcodes(list : List<BarcodeEntity>) {
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

        fun bind(item: BarcodeEntity) {
            itemView.dataTextView.text = item.barcode.value
            itemView.setOnClickListener(this)
            if (item.widgetId.isValidWidgetId()) {
                itemView.background = null
            } else {
                itemView.setBackgroundColor(Color.RED)
            }
        }

        override fun onClick(v: View?) {
            onItemClickListener?.onItemClicked(barcodes[adapterPosition])
        }
    }
}

interface OnItemClickListener {
    fun onItemClicked(barcodeEntity: BarcodeEntity)
}