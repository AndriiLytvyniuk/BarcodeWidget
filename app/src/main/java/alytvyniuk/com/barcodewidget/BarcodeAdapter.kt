package alytvyniuk.com.barcodewidget

import alytvyniuk.com.barcodewidget.converters.CodeToImageConverter
import alytvyniuk.com.barcodewidget.model.Barcode
import alytvyniuk.com.barcodewidget.model.isValidWidgetId
import alytvyniuk.com.barcodewidget.utils.ReusableCompositeDisposable
import android.graphics.Color
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.barcode_list_item.view.*
import kotlinx.android.synthetic.main.header_list_item.view.*

private const val VIEW_TYPE_HEADER = 0
private const val VIEW_TYPE_BARCODE = 1

class BarcodeAdapter(private val codeToImageConverter: CodeToImageConverter,
                     private val reusableDisposable: ReusableCompositeDisposable)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var barcodes : List<Barcode> = listOf()
    private var onItemClickListener : OnItemClickListener? = null
    private var availableCount = 0

    fun setBarcodes(list : List<Barcode>) {
        barcodes = list.sortedBy { it.widgetId }
        availableCount = barcodes.count { !it.widgetId.isValidWidgetId() }
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_HEADER) {
            HeaderItemHolder(parent.inflateWithoutAttach(R.layout.header_list_item))
        } else {
            BarcodeItemHolder(parent.inflateWithoutAttach(R.layout.barcode_list_item))
        }
    }

    override fun getItemViewType(position: Int)
            = if (position == 0 || (availableCount > 0 && position == availableCount + 1)) VIEW_TYPE_HEADER else VIEW_TYPE_BARCODE

    override fun getItemCount() : Int {
        var result = barcodes.size + 1
        if (availableCount > 0 && barcodes.size > availableCount) {
            result ++
        }
        return result
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, index: Int) {
        if (holder is BarcodeItemHolder) {
            val barcode = if (availableCount == 0 || (availableCount > 0 && index <= availableCount)) {
                barcodes[index - 1]
            } else {
                barcodes[index - 2]
            }
            holder.bind(barcode)
        } else if (holder is HeaderItemHolder) {
            holder.bind()
        }
    }

    private fun ViewGroup.inflateWithoutAttach(layoutRes: Int): View {
        return LayoutInflater.from(context).inflate(layoutRes, this, false)
    }

    inner class BarcodeItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        private lateinit var barcode : Barcode

        fun bind(item: Barcode) {
            barcode = item
            itemView.setOnClickListener(this)
            itemView.dataTextView.text = item.rawBarcode.value

            itemView.barcodeImageView.setImageFromBarcode(codeToImageConverter, item.rawBarcode)
            if (TextUtils.isEmpty(item.title)) {
                itemView.titleTextView.visibility = View.INVISIBLE
            } else {
                itemView.titleTextView.visibility = View.VISIBLE
                itemView.titleTextView.text = item.title
            }
            itemView.frameView.setBackgroundColor(item.color ?: Color.TRANSPARENT)
        }

        override fun onClick(v: View) {
            onItemClickListener?.onItemClicked(barcode)
        }
    }

    inner class HeaderItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind() {
            itemView.headerText.text = itemView.context.getText(
                if (adapterPosition == 0 && availableCount > 0)
                    R.string.available_items
                else
                    R.string.reserved_by_widget
            )
        }
    }
}

interface OnItemClickListener {
    fun onItemClicked(barcode: Barcode)
}
