package alytvyniuk.com.barcodewidget.model

import alytvyniuk.com.barcodewidget.db.BarcodeDao
import android.appwidget.AppWidgetManager
import android.os.Parcel
import android.os.Parcelable

data class Barcode(
    var rawBarcode: RawBarcode,
    var widgetId: Int = AppWidgetManager.INVALID_APPWIDGET_ID,
    var id: Int = BarcodeDao.INVALID_DB_ID,
    var title: String = "",
    var color: Int? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readParcelable(RawBarcode::class.java.classLoader),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(rawBarcode, flags)
        parcel.writeInt(widgetId)
        parcel.writeInt(id)
        parcel.writeString(title)
        parcel.writeValue(color)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun toString(): String {
        return "Barcode(rawBarcode=$rawBarcode, widgetId=$widgetId, id=$id, title='$title', color=$color)"
    }

    companion object CREATOR : Parcelable.Creator<Barcode> {
        override fun createFromParcel(parcel: Parcel): Barcode {
            return Barcode(parcel)
        }

        override fun newArray(size: Int): Array<Barcode?> {
            return arrayOfNulls(size)
        }
    }

}

fun Int.isValidWidgetId() = this != AppWidgetManager.INVALID_APPWIDGET_ID
