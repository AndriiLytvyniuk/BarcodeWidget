package alytvyniuk.com.barcodewidget.model

import android.os.Parcel
import android.os.Parcelable

data class RawBarcode(val format : Format, val value: String = "") : Parcelable {

    constructor(parcel: Parcel) : this (
        Format.valueOf(parcel.readString()!!),
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(format.toString())
        parcel.writeString(value)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RawBarcode> {
        override fun createFromParcel(parcel: Parcel): RawBarcode {
            return RawBarcode(parcel)
        }

        override fun newArray(size: Int): Array<RawBarcode?> {
            return arrayOfNulls(size)
        }
    }

    override fun toString(): String {
        return "RawBarcode(format=$format, value='$value')"
    }
}

enum class Format {
    UNKNOWN,
    QR_CODE,
    AZTEC,
    CODABAR,
    CODE_39,
    CODE_93,
    CODE_128,
    DATA_MATRIX,
    EAN_8,
    EAN_13,
    PDF_417,
    UPC_A,
    UPC_E,
    ITF
}