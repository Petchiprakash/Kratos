package com.example.kratos

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.kratos.databinding.ActivityEventManagerBinding

class EventManagerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEventManagerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEventManagerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnEventScanner.setOnClickListener {
            val intent = Intent(this, EventScannerActivity::class.java)
            startActivity(intent)
        }
        binding.btnPlayersList.setOnClickListener {
            val intent = Intent(this, EventListActivity::class.java)
            startActivity(intent)
        }
    }
}