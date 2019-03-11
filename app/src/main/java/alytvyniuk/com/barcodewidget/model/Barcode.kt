package alytvyniuk.com.barcodewidget.model

import android.appwidget.AppWidgetManager
import android.os.Parcel
import android.os.Parcelable

data class Barcode(
    var rawBarcode: RawBarcode,
    var widgetId: Int = AppWidgetManager.INVALID_APPWIDGET_ID,
    var id: Int = INVALID_DB_ID,
    var title: String = "") : Parcelable{

    constructor(parcel: Parcel) : this(
        parcel.readParcelable(RawBarcode::class.java.classLoader)!!,
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(rawBarcode, flags)
        parcel.writeInt(widgetId)
        parcel.writeInt(id)
        parcel.writeString(title)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Barcode> {
        const val INVALID_DB_ID = 0
        override fun createFromParcel(parcel: Parcel): Barcode {
            return Barcode(parcel)
        }

        override fun newArray(size: Int): Array<Barcode?> {
            return arrayOfNulls(size)
        }
    }
}

fun Int.isValidWidgetId() = this != AppWidgetManager.INVALID_APPWIDGET_ID