package alytvyniuk.com.barcodewidget.model

data class Barcode(val format : Format, val value: String = "") {

    override fun toString(): String {
        return "Barcode(format=$format, value='$value')"
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