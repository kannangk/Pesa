package finance.pesa.sdk.view.UI

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import finance.pesa.sdk.R
import finance.pesa.sdk.utils.Constants.QR_CODE_RESULT
import finance.pesa.sdk.utils.UserInterface

class QRCodeScannerActivity : AppCompatActivity() {
    private lateinit var codeScanner: CodeScanner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.qr_code_scaner)
        val ivBack=findViewById<ImageView>(R.id.iv_back)
        val scannerView = findViewById<CodeScannerView>(R.id.scanner_view)
        UserInterface.changeStatusBar(this,R.color.white_black)
        codeScanner = CodeScanner(this, scannerView)

        // Parameters (default values)
        codeScanner.camera = CodeScanner.CAMERA_BACK // or CAMERA_FRONT or specific camera id
        codeScanner.formats = CodeScanner.ALL_FORMATS // list of type BarcodeFormat,
        // ex. listOf(BarcodeFormat.QR_CODE)
        codeScanner.autoFocusMode = AutoFocusMode.SAFE // or CONTINUOUS
        codeScanner.scanMode = ScanMode.SINGLE // or CONTINUOUS or PREVIEW
        codeScanner.isAutoFocusEnabled = true // Whether to enable auto focus or not
        codeScanner.isFlashEnabled = false // Whether to enable flash or not
        UserInterface.changeStatusBar(this, R.color.white_black)
        UserInterface.changeNavigationBar(this, R.color.white_black)
        // Callbacks
        codeScanner.decodeCallback = DecodeCallback {
            runOnUiThread {
                val resultIntent = Intent()
                resultIntent.putExtra(QR_CODE_RESULT, it.text)
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
               // Toast.makeText(this, "Scan result: ${it.text}", Toast.LENGTH_LONG).show()
            }
        }
        codeScanner.errorCallback = ErrorCallback { // or ErrorCallback.SUPPRESS
            runOnUiThread {
                Toast.makeText(this, "Camera initialization error: ${it.message}",
                    Toast.LENGTH_LONG).show()
                finish()
            }
        }
        ivBack.setOnClickListener { finish() }

        scannerView.setOnClickListener {
            codeScanner.startPreview()
        }
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }
}