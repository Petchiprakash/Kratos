package com.example.kratos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.kratos.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnNewUser.setOnClickListener {
            val intent = Intent(this, NewUserActivity::class.java)
            startActivity(intent)
        }
        binding.btnExtUser.setOnClickListener {
            val intent = Intent(this, ExistingUserActivity::class.java)
            startActivity(intent)
        }
        binding.btnEventManager.setOnClickListener {
            val intent = Intent(this, EventManagerActivity::class.java)
            startActivity(intent)
        }
    }
}