package alytvyniuk.com.barcodewidget.converters

import alytvyniuk.com.barcodewidget.model.Barcode
import alytvyniuk.com.barcodewidget.model.Format
import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import com.journeyapps.barcodescanner.BarcodeEncoder
import java.lang.IllegalArgumentException

private const val IMAGE_SIZE = 200

abstract class CodeToImageConverter(looper: Looper) : AsyncConverter<Barcode, Bitmap>(looper)

class ZXingCodeToImageConverter(looper: Looper) : CodeToImageConverter(looper) {
    override fun performConversion(from: Barcode, id : Int) {
        val multiFormatWriter = MultiFormatWriter()
        val format = mapFormat(from.format)
        val hints = HashMap<EncodeHintType, Any>()
        hints[EncodeHintType.ERROR_CORRECTION] = ErrorCorrectionLevel.M
        val bitMatrix = multiFormatWriter.encode(from.value, format, IMAGE_SIZE, IMAGE_SIZE, hints)
        val barcodeEncoder = BarcodeEncoder()
        val bitmap = barcodeEncoder.createBitmap(bitMatrix)
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