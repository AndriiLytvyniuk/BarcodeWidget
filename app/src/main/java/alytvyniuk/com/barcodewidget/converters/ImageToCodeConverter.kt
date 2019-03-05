package alytvyniuk.com.barcodewidget.converters

import alytvyniuk.com.barcodewidget.model.Barcode
import alytvyniuk.com.barcodewidget.model.Format
import android.graphics.Bitmap
import android.os.Looper
import android.util.Log
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.zxing.*
import com.google.zxing.common.HybridBinarizer

private const val TAG = "ImageToCode"
abstract class ImageToCodeConverter (looper: Looper) : AsyncConverter<Bitmap, Barcode>(looper)

class ZxingImageToCodeConverter(looper: Looper) : ImageToCodeConverter(looper) {

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


class MLKitImageToCodeConverter(looper: Looper) : ImageToCodeConverter(looper)  {

    override fun performConversion(from: Bitmap, id : Int) {
        val image = FirebaseVisionImage.fromBitmap(from)
        val options = FirebaseVisionBarcodeDetectorOptions.Builder()
            .setBarcodeFormats(
                FirebaseVisionBarcode.FORMAT_QR_CODE)
            .build()
        val detector = FirebaseVision.getInstance().getVisionBarcodeDetector(options)
        detector.detectInImage(image)
            .addOnSuccessListener { barcodes ->
                Log.d(TAG, "Converted barcodes: size = ${barcodes.size}")
                if (barcodes.size > 0) {
                    sendResult(firebaseBarcodeToBarcode(barcodes[0]), id)
                }
            }
            .addOnFailureListener { e ->
                sendError(e, id)
            }
    }

    private fun firebaseBarcodeToBarcode(firebaseBarcode: FirebaseVisionBarcode) : Barcode {
        val v = if (firebaseBarcode.rawValue != null) firebaseBarcode.rawValue!! else ""
        val format = mapFormat(firebaseBarcode.format)
        val result = Barcode(format, v)
        Log.d(TAG, "result: $result")
        return result
    }

    private fun mapFormat(mlKitFormat: Int) =
        when (mlKitFormat) {
            FirebaseVisionBarcode.FORMAT_QR_CODE -> Format.QR_CODE
            FirebaseVisionBarcode.FORMAT_AZTEC -> Format.QR_CODE
            FirebaseVisionBarcode.FORMAT_CODABAR -> Format.QR_CODE
            FirebaseVisionBarcode.FORMAT_CODE_39 -> Format.QR_CODE
            FirebaseVisionBarcode.FORMAT_CODE_93 -> Format.QR_CODE
            FirebaseVisionBarcode.FORMAT_CODE_128 -> Format.QR_CODE
            FirebaseVisionBarcode.FORMAT_DATA_MATRIX -> Format.QR_CODE
            FirebaseVisionBarcode.FORMAT_EAN_8 -> Format.QR_CODE
            FirebaseVisionBarcode.FORMAT_EAN_13 -> Format.QR_CODE
            FirebaseVisionBarcode.FORMAT_PDF417 -> Format.QR_CODE
            FirebaseVisionBarcode.FORMAT_UPC_A -> Format.QR_CODE
            FirebaseVisionBarcode.FORMAT_UPC_E -> Format.QR_CODE
            FirebaseVisionBarcode.FORMAT_ITF -> Format.QR_CODE
            else -> Format.UNKNOWN
        }
}

class StubImageToCodeConverter(looper: Looper) : ImageToCodeConverter(looper) {
    override fun performConversion(from: Bitmap, id: Int) {
        sendResult(Barcode(Format.QR_CODE, "Andrii"), id)
    }
}