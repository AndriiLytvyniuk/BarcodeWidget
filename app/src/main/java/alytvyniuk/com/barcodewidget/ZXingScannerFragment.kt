package alytvyniuk.com.barcodewidget

import alytvyniuk.com.barcodewidget.converters.ZxingImageToCodeConverter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.zxing.Result
import me.dm7.barcodescanner.zxing.ZXingScannerView

private const val TAG = "ScannerFragment"

class ZXingScannerFragment : Fragment(), ZXingScannerView.ResultHandler {
    private lateinit var scannerView: ZXingScannerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        scannerView = ZXingScannerView(activity!!)
        return scannerView
    }

    override fun onResume() {
        super.onResume()
        if (activity?.hasCameraPermission() == true) {
            scannerView.setResultHandler(this)
            scannerView.startCamera()
        }
    }

    override fun handleResult(zxingResult: Result) {
        Log.d(TAG, "handleResult ${zxingResult.barcodeFormat} + ${zxingResult.text}")
        val barcode = ZxingImageToCodeConverter.zxingBarcodeToBarcode(zxingResult)
        if (activity != null) {
            val resultHandler = activity as BarcodeResultHandler
            resultHandler.onBarcodeResult(barcode)
        }
    }

    override fun onPause() {
        super.onPause()
        scannerView.stopCamera()
    }
}
