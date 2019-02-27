package alytvyniuk.com.barcodewidget.converters

import alytvyniuk.com.barcodewidget.model.Barcode
import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.journeyapps.barcodescanner.BarcodeEncoder

abstract class CodeToImageConverter(looper: Looper) : AsyncConverter<Barcode, Bitmap>(looper)

class ZXingCodeToImageConverter(looper: Looper) : CodeToImageConverter(looper) {
    override fun performConversion(from: Barcode, id : Int) {
        val multiFormatWriter = MultiFormatWriter()

        val bitMatrix = multiFormatWriter.encode(from.value, BarcodeFormat.QR_CODE, 200, 200)
        val barcodeEncoder = BarcodeEncoder()
        val bitmap = barcodeEncoder.createBitmap(bitMatrix)
        sendResult(bitmap, id)
    }
}