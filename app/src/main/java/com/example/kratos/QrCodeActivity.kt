package com.example.kratos

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.*
import com.example.kratos.databinding.ActivityQrCodeBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class QrCodeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityQrCodeBinding
    private lateinit var codeScanner: CodeScanner
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQrCodeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) ==
            PackageManager.PERMISSION_DENIED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.CAMERA),
                123
            )
        } else {
            startScanning()
        }
    }

    private fun startScanning() {
        val scannerView: CodeScannerView = binding.scannerView
        codeScanner = CodeScanner(this, scannerView)
        codeScanner.apply {
            camera = CodeScanner.CAMERA_BACK
            formats = CodeScanner.ALL_FORMATS
            autoFocusMode = AutoFocusMode.SAFE
            scanMode = ScanMode.SINGLE
            isAutoFocusEnabled = true
            isFlashEnabled = false
            decodeCallback = DecodeCallback {
                runOnUiThread {
                    Toast.makeText(
                        this@QrCodeActivity,
                        "Scan Result: ${it.text}",
                        Toast.LENGTH_LONG
                    ).show()
                    val email = intent.getStringExtra("email")
                    val mobileNum = intent.getStringExtra("mobileNum")
                    var toggle = false
                    val qrCode = it.text
                    database = FirebaseDatabase.getInstance().getReference("Participants")
                    database.get().addOnSuccessListener {
                        for (participantList in it.children) {
                            if (email == participantList.child("email").value && mobileNum == participantList.child(
                                    "mobileNo"
                                ).value
                            ) {
                                toggle = true
                                val user = mapOf(
                                    "QrCode" to qrCode
                                )
                                if (mobileNum != null) {
                                    database.child(mobileNum).updateChildren(user)
                                    Toast.makeText(
                                        this@QrCodeActivity,
                                        "Thanks For Participating ",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                val intent =
                                    Intent(this@QrCodeActivity, MainActivity::class.java)
                                startActivity(intent)
                            }
                        }
                        if (!toggle) {
                            Toast.makeText(
                                this@QrCodeActivity,
                                "QR Code is Not Mapped",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }.addOnFailureListener {
                        Toast.makeText(
                            this@QrCodeActivity,
                            "Failed To Fetch Data",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
            errorCallback = ErrorCallback {
                runOnUiThread {
                    Toast.makeText(
                        this@QrCodeActivity,
                        "Camera initialization error: ${it.message} ",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        scannerView.setOnClickListener {
            codeScanner.startPreview()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 123) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Camera Permission granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Camera Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (::codeScanner.isInitialized) {
            codeScanner.startPreview()
        }
    }

    override fun onPause() {
        if (::codeScanner.isInitialized) {
            codeScanner.releaseResources()
        }
        super.onPause()
    }
}