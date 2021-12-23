package com.example.kratos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.kratos.databinding.ActivityNewUserBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class NewUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewUserBinding
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnSubmit.setOnClickListener {
            val firstName = binding.etFirstName.text.toString()
            val lastName = binding.etLastName.text.toString()
            val phoneNumber = binding.etMobileNo.text.toString()
            val email = binding.etEmailId.text.toString()

            when {
                firstName.isEmpty() -> {
                    binding.etFirstName.error = "Enter FirstName"
                    binding.etFirstName.requestFocus()
                }
                lastName.isEmpty() -> {
                    binding.etLastName.error = "Enter LastName"
                    binding.etLastName.requestFocus()
                }
                phoneNumber.isEmpty() -> {
                    binding.etMobileNo.error = "Enter Mobile Number"
                    binding.etMobileNo.requestFocus()
                }
                email.isEmpty() -> {
                    binding.etEmailId.error = "Enter Email ID"
                    binding.etEmailId.requestFocus()
                }
                else -> {
                    var toggle = false
                    database = FirebaseDatabase.getInstance().getReference("Participants")
                    val user = ParticipantsData(firstName, lastName, phoneNumber, email)
                    database.get().addOnSuccessListener {
                        for (participantList in it.children) {
                            if (email == participantList.child("email").value ||
                                phoneNumber == participantList.child("mobileNo").value
                            ) {
                                toggle = true
                                Toast.makeText(this, "User Already Exist", Toast.LENGTH_LONG).show()
                            }
                        }
                        if (!toggle) {
                            database.child(phoneNumber).setValue(user).addOnSuccessListener {

                                binding.etFirstName.text.clear()
                                binding.etLastName.text.clear()
                                binding.etMobileNo.text.clear()
                                binding.etEmailId.text.clear()

                                Toast.makeText(this, "Successfully Saved", Toast.LENGTH_SHORT)
                                    .show()
                                val intent = Intent(this, QrCodeActivity::class.java)
                                intent.apply {
                                    putExtra("mobileNum", phoneNumber)
                                    putExtra("email", email)
                                }
                                startActivity(intent)
                            }.addOnCanceledListener {
                                Toast.makeText(this, "Failed To Save", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }
            }
        }
    }
}