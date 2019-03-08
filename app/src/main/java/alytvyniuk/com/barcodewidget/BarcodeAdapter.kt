package alytvyniuk.com.barcodewidget

import alytvyniuk.com.barcodewidget.model.BarcodeEntity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.barcode_list_item.view.*

class BarcodeAdapter : RecyclerView.Adapter<BarcodeAdapter.BarcodeItemHolder>() {

    private var barcodes : List<BarcodeEntity> = listOf()

    fun setBarcodes(list : List<BarcodeEntity>) {
        barcodes = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int)
            = BarcodeItemHolder(parent.inflateWithoutAttach(R.layout.barcode_list_item))

    override fun getItemCount() = barcodes.size

    override fun onBindViewHolder(holder: BarcodeItemHolder, index: Int) = holder.bind(barcodes[index])

    private fun ViewGroup.inflateWithoutAttach(layoutRes: Int): View {
        return LayoutInflater.from(context).inflate(layoutRes, this, false)
    }

    class BarcodeItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: BarcodeEntity) {
            itemView.dataTextView.text = item.barcode.value
            itemView.setOnClickListener {
                val intent = EditActivity.intent(itemView.context, item.barcode, item.widgetId)
                itemView.context.startActivity(intent)
            }
        }
    }
}