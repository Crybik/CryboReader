package com.example.cryqr

import android.Manifest.permission.CAMERA
import android.content.ClipData
import android.content.ClipboardManager
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import br.fgr.customqrcode.CustomQrCode
import com.budiyev.android.codescanner.*
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.google.android.gms.vision.Frame
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView


private  const val CAMERA_REQUEST_CODE = 101

class MainActivity : AppCompatActivity() {
    private lateinit var codeScanner: CodeScanner
    lateinit var mBitmap : Bitmap
    lateinit var button: Button
    lateinit var img: ImageView

    private val pickImage = 100
    private var imageUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupPermissions()
        codeScanner()
        copy.setOnClickListener {
            val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("EditText", tv_textView.getText().toString())
            clipboard.setPrimaryClip(clip)
            Toast.makeText(this, "text copied",Toast.LENGTH_SHORT).show()
            // the thing starts here

            // the things ends here

        }

    }

    private fun codeScanner() {
        codeScanner = CodeScanner( this, scanner_view)
        codeScanner.apply {
            camera = CodeScanner.CAMERA_BACK
            formats = CodeScanner.ALL_FORMATS
            autoFocusMode = AutoFocusMode.SAFE
            scanMode = ScanMode.CONTINUOUS
            isAutoFocusEnabled = true
            isFlashEnabled = false
            decodeCallback = DecodeCallback {
                runOnUiThread {
                    tv_textView.text = it.text
                }
            }
            errorCallback = ErrorCallback {
                runOnUiThread {
                    Log.e("Main", "Camera initialiazation error ${it.message}")
                }
            }
        }
        scanner_view.setOnClickListener {
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
    private fun setupPermissions() {
        val permission = ContextCompat.checkSelfPermission( this, CAMERA)
        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest()
        }
    }
    private fun makeRequest(){
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), CAMERA_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode) {
            CAMERA_REQUEST_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                  Toast.makeText(this, "You need Camera permission to use this app", Toast.LENGTH_SHORT)
                } else {
                    // it works : )
                }
            }
        }
    }


}