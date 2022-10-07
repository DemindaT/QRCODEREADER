package com.example.qrcodereader


import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode




class MainActivity : AppCompatActivity() {

    private lateinit var codeScanner: CodeScanner
    private lateinit var scannerView: CodeScannerView
    private lateinit var textView: TextView

    val myCamera = 101
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        scannerView = findViewById(R.id.scannerView)
        textView = findViewById(R.id.textView)
        codeScanner = CodeScanner(this, scannerView)

        codeScanner.apply {

        camera = CodeScanner.CAMERA_BACK
        formats = CodeScanner.ALL_FORMATS
        autoFocusMode = AutoFocusMode.SAFE
        scanMode = ScanMode.CONTINUOUS
        isAutoFocusEnabled = true
        isFlashEnabled = true

        decodeCallback = DecodeCallback {
            runOnUiThread{
                textView.text= it.text

            }

        }

        errorCallback = ErrorCallback {
            runOnUiThread{
                Log.e("Main", "Camera initialization error: ${it.message}")
            }

        }
        checkPermission()
    }
        scannerView.setOnClickListener{
            codeScanner.startPreview()
        }
    }

    fun checkPermission(){
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), myCamera)

        } else {
            codeScanner.startPreview()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==myCamera&&grantResults.isNotEmpty()&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
            codeScanner.startPreview()
        }else{
            Toast.makeText(this, "Can not scan until you give the camera permission ", Toast.LENGTH_LONG ).show()
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