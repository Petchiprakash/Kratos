package com.example.kratos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.kratos.databinding.ActivityExistingUserBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ExistingUserActivity : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var binding: ActivityExistingUserBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExistingUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnValidate.setOnClickListener {
            val email: String = binding.etEmailId.text.toString()
            val mobileNum: String = binding.etMobileNo.text.toString()
            when {
                mobileNum.isEmpty() -> {
                    binding.etMobileNo.error = "Enter Mobile Number"
                    binding.etMobileNo.requestFocus()
                }
                email.isEmpty() -> {
                    binding.etEmailId.error = "Enter Email ID"
                    binding.etEmailId.requestFocus()
                }
                else -> {
                    validateData(email, mobileNum)
                }
            }
        }
    }

    private fun validateData(email: String, mobileNum: String) {
        var toggle = false
        database = FirebaseDatabase.getInstance().getReference("Participants")
        database.get().addOnSuccessListener {
            for (participantList in it.children) {
                if (email == participantList.child("email").value && mobileNum == participantList.child(
                        "mobileNo"
                    ).value
                ) {
                    binding.etEmailId.text.clear()
                    binding.etMobileNo.text.clear()
                    val firstName = participantList.child("firstName").value
                    Toast.makeText(this, "Welcome! $firstName ", Toast.LENGTH_SHORT).show()
                    toggle = true
                    val intent = Intent(this, QrCodeActivity::class.java)
                    intent.apply {
                        putExtra("mobileNum", mobileNum)
                        putExtra("email", email)
                    }
                    startActivity(intent)
                }
            }
            if (!toggle) {
                Toast.makeText(this, "Check the Spelling or Register!", Toast.LENGTH_LONG).show()
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Failed To Fetch Data", Toast.LENGTH_LONG).show()
        }

    }
}