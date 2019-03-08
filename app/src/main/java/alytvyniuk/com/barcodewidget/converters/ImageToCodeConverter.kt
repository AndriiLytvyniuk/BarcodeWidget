package alytvyniuk.com.barcodewidget.converters

import alytvyniuk.com.barcodewidget.model.Barcode
import alytvyniuk.com.barcodewidget.model.Format
import android.graphics.Bitmap
import android.os.Looper
import com.google.zxing.*
import com.google.zxing.common.HybridBinarizer

private const val TAG = "ImageToCode"
abstract class ImageToCodeConverter (looper: Looper) : AsyncConverter<Bitmap, Barcode>(looper)

class ZxingImageToCodeConverter(looper: Looper) : ImageToCodeConverter(looper) {

    companion object {
        fun zxingBarcodeToBarcode(zxingResult: Result) =
            Barcode(mapFormat(zxingResult.barcodeFormat), zxingResult.text)

        private fun mapFormat(zxingFormat: BarcodeFormat) =
            when (zxingFormat) {
                BarcodeFormat.QR_CODE -> Format.QR_CODE
                BarcodeFormat.AZTEC -> Format.AZTEC
                BarcodeFormat.CODABAR -> Format.CODABAR
                BarcodeFormat.CODE_39 -> Format.CODE_39
                BarcodeFormat.CODE_93 -> Format.CODE_93
                BarcodeFormat.CODE_128 -> Format.CODE_128
                BarcodeFormat.DATA_MATRIX -> Format.DATA_MATRIX
                BarcodeFormat.EAN_8 -> Format.EAN_8
                BarcodeFormat.EAN_13 -> Format.EAN_13
                BarcodeFormat.PDF_417 -> Format.PDF_417
                BarcodeFormat.UPC_A -> Format.UPC_A
                BarcodeFormat.UPC_E -> Format.UPC_E
                BarcodeFormat.ITF -> Format.ITF
                else -> throw IllegalArgumentException("Unknown format from zxing: $zxingFormat")
            }
    }

    override fun performConversion(bitmap: Bitmap, id: Int) {
        try {
            val width = bitmap.width
            val height = bitmap.height
            val pixels = IntArray(width * height)
            bitmap.getPixels(pixels, 0, width, 0, 0, width, height)
            val source = RGBLuminanceSource(width, height, pixels)
            val binaryBitmap = BinaryBitmap(HybridBinarizer(source))
            val result = MultiFormatReader().decode(binaryBitmap)
            val barcode = zxingBarcodeToBarcode(result)
            sendResult(barcode, id)
        } catch (e: Exception) {
            sendError(e, id)
        }
    }
}

class StubImageToCodeConverter(looper: Looper) : ImageToCodeConverter(looper) {
    override fun performConversion(from: Bitmap, id: Int) {
        sendResult(Barcode(Format.QR_CODE, "Andrii"), id)
    }
}