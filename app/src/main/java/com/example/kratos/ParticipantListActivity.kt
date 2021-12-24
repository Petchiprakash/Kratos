package com.example.kratos

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kratos.databinding.ActivityParticipantListBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ParticipantListActivity : AppCompatActivity() {
    private lateinit var databaseReference: DatabaseReference
    private lateinit var participantArrayList: ArrayList<ParticipantsData>
    private lateinit var participantRecyclerView: RecyclerView
    private lateinit var binding: ActivityParticipantListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityParticipantListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        participantRecyclerView = binding.participantsList
        participantRecyclerView.layoutManager = LinearLayoutManager(this)
        participantRecyclerView.setHasFixedSize(true)
        participantArrayList = arrayListOf()
        getParticipantsData()

    }
    private fun getParticipantsData() {
        val eventName = intent.getStringExtra("eventName")
        databaseReference = FirebaseDatabase.getInstance().getReference("Participants")
        databaseReference.get().addOnSuccessListener {
            for (participantList in it.children) {
                when (eventName) {
                    participantList.child("event1").value -> participantList.getValue(
                        ParticipantsData::class.java
                    )
                        ?.let { it1 -> participantArrayList.add(it1) }
                    participantList.child("event2").value -> participantList.getValue(
                        ParticipantsData::class.java
                    )
                        ?.let { it1 -> participantArrayList.add(it1) }
                    participantList.child("event3").value -> participantList.getValue(
                        ParticipantsData::class.java
                    )
                        ?.let { it1 -> participantArrayList.add(it1) }
                    participantList.child("event4").value -> participantList.getValue(
                        ParticipantsData::class.java
                    )
                        ?.let { it1 -> participantArrayList.add(it1) }
                    participantList.child("event5").value -> participantList.getValue(
                        ParticipantsData::class.java
                    )
                        ?.let { it1 -> participantArrayList.add(it1) }
                }
            }
           val myAdapter = ParticipantsAdapter(participantArrayList,this)
            participantRecyclerView.adapter = myAdapter
            val actionBar : ActionBar? = supportActionBar
            actionBar?.setDisplayHomeAsUpEnabled(true)
            actionBar?.setDisplayShowHomeEnabled(true)
            actionBar?.title = eventName
        }.addOnFailureListener {
            Toast.makeText(
                this,
                "Failed To Fetch Data",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}
