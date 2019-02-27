package alytvyniuk.com.barcodewidget.converters

import alytvyniuk.com.barcodewidget.model.Barcode
import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode
import com.google.firebase.ml.vision.common.FirebaseVisionImage

abstract class ImageToCodeConverter (looper: Looper) : AsyncConverter<Bitmap, Barcode>(looper)

class MLKitImageToCodeConverter(looper: Looper) : ImageToCodeConverter(looper)  {

    override fun performConversion(from: Bitmap, id : Int) {
        val image = FirebaseVisionImage.fromBitmap(from)
        val detector = FirebaseVision.getInstance().visionBarcodeDetector
        detector.detectInImage(image)
            .addOnSuccessListener { barcodes ->
                if (barcodes.size > 0) {
                    sendResult(firebaseBarcodeToBarcode(barcodes[0]), id)
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