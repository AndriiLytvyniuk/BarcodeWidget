package alytvyniuk.com.barcodewidget.converters

import alytvyniuk.com.barcodewidget.model.Format
import alytvyniuk.com.barcodewidget.model.RawBarcode
import android.graphics.Bitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import com.journeyapps.barcodescanner.BarcodeEncoder
import javax.inject.Inject
import javax.inject.Singleton

private const val IMAGE_SIZE = 200

interface CodeToImageConverter : Converter<RawBarcode, Bitmap>

@Singleton
class ZXingCodeToImageConverter @Inject constructor(): CodeToImageConverter {

    override suspend fun convert(rawBarcode: RawBarcode): Bitmap {
        val multiFormatWriter = MultiFormatWriter()
        val format = mapFormat(rawBarcode.format)
        val width = when (format) {
            BarcodeFormat.CODABAR,
            BarcodeFormat.EAN_13 -> IMAGE_SIZE * 2
            else -> IMAGE_SIZE
        }
        val options = if (format == BarcodeFormat.QR_CODE)
            mapOf<EncodeHintType, Any>(EncodeHintType.ERROR_CORRECTION to ErrorCorrectionLevel.L).toMutableMap()
        else
            null
        val bitMatrix = multiFormatWriter.encode(rawBarcode.value, format, width, IMAGE_SIZE, options)
        val barcodeEncoder = BarcodeEncoder()
        return barcodeEncoder.createBitmap(bitMatrix)
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
