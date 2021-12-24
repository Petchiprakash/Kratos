package com.example.kratos

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kratos.databinding.ActivityEventListBinding

class EventListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEventListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEventListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val listView = binding.listView
        val events = arrayOf("Live Trivia in General Session","Digital Trade Show Passport","Scavenger Hunt",
            "Name Wheel","MidCity Madness")
        val arrayAdapter: ArrayAdapter<String> = ArrayAdapter(
            this, android.R.layout.simple_list_item_1, events
        )
        listView.adapter = arrayAdapter
        listView.setOnItemClickListener { parent, view, position, id ->
                val intent = Intent(this, ParticipantListActivity::class.java)
                intent.putExtra("eventName",events[position])
                startActivity(intent)
        }

    }
}