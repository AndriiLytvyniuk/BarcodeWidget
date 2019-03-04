package alytvyniuk.com.barcodewidget.converters

import alytvyniuk.com.barcodewidget.model.Barcode
import alytvyniuk.com.barcodewidget.model.Format
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Looper
import android.util.Log
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions
import com.google.firebase.ml.vision.common.FirebaseVisionImage

private const val TAG = "ImageToCode"
abstract class ImageToCodeConverter (looper: Looper) : AsyncConverter<Bitmap, Barcode>(looper) {

    abstract fun cv(context: Context, uri : Uri)
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

    override fun cv(context: Context, uri : Uri) {
        val image = FirebaseVisionImage.fromFilePath(context, uri)
        val options = FirebaseVisionBarcodeDetectorOptions.Builder()
            .setBarcodeFormats(
                FirebaseVisionBarcode.FORMAT_QR_CODE)
            .build()
        Log.d(TAG, "Cv: start")

        val detector = FirebaseVision.getInstance().getVisionBarcodeDetector(options)
        detector.detectInImage(image)
            .addOnSuccessListener { barcodes ->
                Log.d(TAG, "Converted barcodes: size = ${barcodes.size}")
                for (barcode in barcodes) {
                    Log.d(TAG, "Barcode = $barcode")
                }
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