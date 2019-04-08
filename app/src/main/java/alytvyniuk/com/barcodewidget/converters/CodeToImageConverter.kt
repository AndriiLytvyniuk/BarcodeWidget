package alytvyniuk.com.barcodewidget.converters

import alytvyniuk.com.barcodewidget.model.Format
import alytvyniuk.com.barcodewidget.model.RawBarcode
import android.graphics.Bitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.journeyapps.barcodescanner.BarcodeEncoder
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton

private const val IMAGE_SIZE = 200

interface CodeToImageConverter : Converter<RawBarcode, Observable<Bitmap>>

@Singleton
class ZXingCodeToImageConverter @Inject constructor(): CodeToImageConverter {

    override fun convert(rawBarcode: RawBarcode): Observable<Bitmap> {
        val multiFormatWriter = MultiFormatWriter()
        val format = mapFormat(rawBarcode.format)
        val bitMatrix = multiFormatWriter.encode(rawBarcode.value, format, IMAGE_SIZE, IMAGE_SIZE)
        val barcodeEncoder = BarcodeEncoder()
        return Observable.fromCallable {
            barcodeEncoder.createBitmap(bitMatrix)
        }
    }

    @SuppressWarnings("ComplexMethod")
    private fun mapFormat(format: Format) =
        when (format) {
            Format.QR_CODE -> BarcodeFormat.QR_CODE
            Format.AZTEC -> BarcodeFormat.AZTEC
            Format.CODABAR -> BarcodeFormat.CODABAR
            Format.CODE_39 -> BarcodeFormat.CODE_39
            Format.CODE_93 -> BarcodeFormat.CODE_93
            Format.CODE_128 -> BarcodeFormat.CODE_128
            Format.DATA_MATRIX -> BarcodeFormat.DATA_MATRIX
            Format.EAN_8 -> BarcodeFormat.EAN_8
            Format.EAN_13 -> BarcodeFormat.EAN_13
            Format.PDF_417 -> BarcodeFormat.PDF_417
            Format.UPC_A -> BarcodeFormat.UPC_A
            Format.UPC_E -> BarcodeFormat.UPC_E
            Format.ITF -> BarcodeFormat.ITF
            else -> throw IllegalArgumentException("Unknown format for zxing: $format")
        }
}
