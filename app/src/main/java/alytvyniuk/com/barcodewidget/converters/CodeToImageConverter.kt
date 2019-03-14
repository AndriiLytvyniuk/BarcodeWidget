package alytvyniuk.com.barcodewidget.converters

import alytvyniuk.com.barcodewidget.R
import alytvyniuk.com.barcodewidget.model.RawBarcode
import alytvyniuk.com.barcodewidget.model.Format
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Looper
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.journeyapps.barcodescanner.BarcodeEncoder
import javax.inject.Inject
import javax.inject.Singleton

private const val IMAGE_SIZE = 200

abstract class CodeToImageConverter(looper: Looper) : AsyncConverter<RawBarcode, Bitmap>(looper) {

    abstract fun convert(rawBarcode: RawBarcode) : Bitmap
}

@Singleton
class ZXingCodeToImageConverter @Inject constructor(looper: Looper) : CodeToImageConverter(looper) {

    override fun convert(rawBarcode: RawBarcode): Bitmap {
        val multiFormatWriter = MultiFormatWriter()
        val format = mapFormat(rawBarcode.format)
        val bitMatrix = multiFormatWriter.encode(rawBarcode.value, format, IMAGE_SIZE, IMAGE_SIZE)
        val barcodeEncoder = BarcodeEncoder()
        return barcodeEncoder.createBitmap(bitMatrix)
    }

    override fun performConversion(from: RawBarcode, id : Int) {
        val bitmap = convert(from)
        sendResult(bitmap, id)
    }

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

class StubCodeToImageConverter(looper: Looper, context: Context) : CodeToImageConverter(looper) {

    private val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.test_qr_code)
    override fun convert(rawBarcode: RawBarcode): Bitmap {
        return bitmap
    }

    override fun performConversion(from: RawBarcode, id: Int) {
        sendResult(bitmap, id)
    }

}