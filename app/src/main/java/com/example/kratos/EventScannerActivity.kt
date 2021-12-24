package com.example.kratos

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.*
import com.example.kratos.databinding.ActivityEventScannerBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class EventScannerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEventScannerBinding
    private lateinit var codeScanner: CodeScanner
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEventScannerBinding.inflate(layoutInflater)
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
                    val qrCode = it.text
                    var toggle = false
                    database = FirebaseDatabase.getInstance().getReference("Participants")
                    database.get().addOnSuccessListener {
                        for (participantList in it.children) {
                            if (qrCode == participantList.child("QrCode").value) {
                                val firstName = participantList.child("firstName").value
                                val lastName = participantList.child("lastName").value
                                Toast.makeText(
                                    this@EventScannerActivity,
                                    "$firstName $lastName ",
                                    Toast.LENGTH_LONG
                                ).show()
                                toggle = true
                                val intent = Intent(this@EventScannerActivity, EventGameActivity::class.java)
                                intent.apply {
                                    putExtra("mobileNum",
                                        participantList.child("mobileNo").value as String?
                                    )
                                    putExtra("email",
                                        participantList.child("email").value as String?
                                    )
                                }
                                startActivity(intent)
                            }
                        }
                        if (!toggle){
                            Toast.makeText(this@EventScannerActivity, "Please Check at the Registration Desk!", Toast.LENGTH_LONG).show()
                        }
                    }
                }
                errorCallback = ErrorCallback {
                    runOnUiThread {
                        Toast.makeText(
                            this@EventScannerActivity,
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