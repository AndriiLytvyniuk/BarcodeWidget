package alytvyniuk.com.barcodewidget.model

import android.appwidget.AppWidgetManager
import android.os.Parcel
import android.os.Parcelable

data class BarcodeEntity(
    var barcode: Barcode,
    var widgetId: Int = AppWidgetManager.INVALID_APPWIDGET_ID,
    var id: Int = INVALID_DB_ID,
    var note: String = "") : Parcelable{

    constructor(parcel: Parcel) : this(
        parcel.readParcelable(Barcode::class.java.classLoader)!!,
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(barcode, flags)
        parcel.writeInt(widgetId)
        parcel.writeInt(id)
        parcel.writeString(note)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BarcodeEntity> {
        const val INVALID_DB_ID = 0
        override fun createFromParcel(parcel: Parcel): BarcodeEntity {
            return BarcodeEntity(parcel)
        }

        override fun newArray(size: Int): Array<BarcodeEntity?> {
            return arrayOfNulls(size)
        }
    }
}

fun Int.isValidWidgetId() = this != AppWidgetManager.INVALID_APPWIDGET_ID