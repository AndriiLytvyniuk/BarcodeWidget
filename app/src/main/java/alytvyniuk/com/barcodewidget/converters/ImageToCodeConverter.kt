package alytvyniuk.com.barcodewidget.converters

import alytvyniuk.com.barcodewidget.model.Barcode
import android.graphics.Bitmap
import android.os.Handler
import android.util.Log
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode
import com.google.firebase.ml.vision.common.FirebaseVisionImage

abstract class ImageToCodeConverter (handler: Handler) : AsyncConverter<Bitmap, Barcode>(handler)

class MLKitImageToCodeConverter(handler: Handler) : ImageToCodeConverter(handler)  {

    override fun performConversion(from: Bitmap) {
        val image = FirebaseVisionImage.fromBitmap(from)
        val detector = FirebaseVision.getInstance().visionBarcodeDetector
        detector.detectInImage(image)
            .addOnSuccessListener { barcodes ->
                if (barcodes.size > 0) {
                    sendResult(firebaseBarcodeToBarcode(barcodes[0]))
                }
            }
            .addOnFailureListener { e ->
                Log.d("Andrii", "onFailure", e)
            }
    }

    private fun firebaseBarcodeToBarcode(firebaseBarcode: FirebaseVisionBarcode) : Barcode {
        val v = if (firebaseBarcode.rawValue != null) firebaseBarcode.rawValue!! else ""
        Log.d("Andrii", "firebaseBarcodeToBarcode: $v")
        return Barcode(firebaseBarcode.format.toString(), v)
    }
}