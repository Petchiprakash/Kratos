package com.example.kratos

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kratos.databinding.ActivityEventGameBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class EventGameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEventGameBinding
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEventGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val email = intent.getStringExtra("email")
        val mobileNum = intent.getStringExtra("mobileNum")
        binding.btnSubmit.setOnClickListener {
            var toggle = false
            database = FirebaseDatabase.getInstance().getReference("Participants")
            var event1 : String? = null
            var event2 : String? = null
            var event3 : String? = null
            var event4 : String? = null
            var event5 : String? = null

            when(true){
                binding.rbChoose1.isChecked -> event1 = binding.rbChoose1.text as String?
                binding.rbChoose2.isChecked -> event2 = binding.rbChoose2.text as String?
                binding.rbChoose3.isChecked -> event3 = binding.rbChoose3.text as String?
                binding.rbChoose4.isChecked -> event4 = binding.rbChoose4.text as String?
                binding.rbChoose5.isChecked -> event5 = binding.rbChoose5.text as String?

                else -> {Toast.makeText(this,"Please select option",Toast.LENGTH_LONG).show()}
            }
            database.get().addOnSuccessListener {
                for (participantList in it.children) {
                    if (email == participantList.child("email").value && mobileNum == participantList.child(
                            "mobileNo"
                        ).value
                    ) {
                        toggle = true
                        val user = mapOf(
                            "event1" to event1,
                            "event2" to event2,
                            "event3" to event3,
                            "event4" to event4,
                            "event5" to event5
                        )
                        if (mobileNum != null) {
                            database.child(mobileNum).updateChildren(user)
                            Toast.makeText(
                                this,
                                "Thanks For Participating in event ",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        val intent =
                            Intent(this, EventManagerActivity::class.java)
                        startActivity(intent)
                    }
                }
                if (!toggle) {
                    Toast.makeText(
                        this,
                        "Not Participated",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }.addOnFailureListener {
                Toast.makeText(
                    this,
                    "Failed To Fetch Data",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

    }
}