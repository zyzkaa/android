package pl.edu.am_projekt.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult

class BarcodeScanActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Start the scan immediately
        IntentIntegrator(this).apply {
            setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
            setPrompt("Zeskanuj kod kreskowy")
            setCameraId(0)
            setBeepEnabled(true)
            setOrientationLocked(false)
            setBarcodeImageEnabled(false)
            initiateScan()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result: IntentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents != null) {
                val intent = Intent().apply {
                    putExtra("barcode", result.contents)
                }
                setResult(Activity.RESULT_OK, intent)
            } else {
                setResult(Activity.RESULT_CANCELED)
            }
//            finish()
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}
