package alytvyniuk.com.barcodewidget.converters

import alytvyniuk.com.barcodewidget.model.Barcode
import android.graphics.Bitmap
import android.os.Handler
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.journeyapps.barcodescanner.BarcodeEncoder

abstract class CodeToImageConverter(handler: Handler) : AsyncConverter<Barcode, Bitmap>(handler)

class ZXingCodeToImageConverter(handler: Handler) : CodeToImageConverter(handler) {
    override fun performConversion(from: Barcode) {
        val multiFormatWriter = MultiFormatWriter()

        val bitMatrix = multiFormatWriter.encode(from.value, BarcodeFormat.QR_CODE, 200, 200)
        val barcodeEncoder = BarcodeEncoder()
        val bitmap = barcodeEncoder.createBitmap(bitMatrix)
        sendResult(bitmap)
    }
}