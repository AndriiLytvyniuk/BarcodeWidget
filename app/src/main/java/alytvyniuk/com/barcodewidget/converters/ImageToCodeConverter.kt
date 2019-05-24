package alytvyniuk.com.barcodewidget.converters

import alytvyniuk.com.barcodewidget.model.Format
import alytvyniuk.com.barcodewidget.model.RawBarcode
import android.graphics.Bitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.BinaryBitmap
import com.google.zxing.MultiFormatReader
import com.google.zxing.Result
import com.google.zxing.RGBLuminanceSource
import com.google.zxing.common.HybridBinarizer
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "ImageToCode"

interface ImageToCodeConverter : Converter<Bitmap, RawBarcode>

@Singleton
class ZxingImageToCodeConverter @Inject constructor() : ImageToCodeConverter {

    companion object {
        fun zxingBarcodeToBarcode(zxingResult: Result) =
            RawBarcode(mapFormat(zxingResult.barcodeFormat), zxingResult.text)

        @SuppressWarnings("ComplexMethod")
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

    @Throws(Exception::class)
    override suspend fun convert(bitmap: Bitmap) = convertBitmapToBarcode(bitmap)

    @Throws(Exception::class)
    private fun convertBitmapToBarcode(bitmap: Bitmap) : RawBarcode {
        val width = bitmap.width
        val height = bitmap.height
        val pixels = IntArray(width * height)
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height)
        val source = RGBLuminanceSource(width, height, pixels)
        val binaryBitmap = BinaryBitmap(HybridBinarizer(source))
        val result = MultiFormatReader().decode(binaryBitmap)
        return zxingBarcodeToBarcode(result)
    }
}
