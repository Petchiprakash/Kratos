package com.example.kratos

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.kratos.databinding.ActivityRoundCheckBinding

class RoundCheckActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRoundCheckBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRoundCheckBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val participantIntent = intent
        val eventName = participantIntent.getStringArrayExtra("event")
        binding.tvEventName.text = eventName.toString()
    }
}