package com.example.kotlinfirebaselogin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.kotlinfirebaselogin.ModelClass.NoteItem
import com.example.kotlinfirebaselogin.databinding.ActivityAddNoteBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddNote : AppCompatActivity() {
    private val binding: ActivityAddNoteBinding by lazy {
        ActivityAddNoteBinding.inflate(layoutInflater)
    }

    private lateinit var databaseReference: DatabaseReference

    //variable to show each user's data
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //initialize firebase database reference
        databaseReference = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()

        binding.btnSave.setOnClickListener {

            //get value from edit text
            val tittle = binding.etTittle.text.toString()
            val description = binding.etDescription.text.toString()

            if (tittle.isEmpty()) {
                binding.etTittle.setError("Required")
            }
            if (description.isEmpty()) {
                binding.etDescription.setError("Required")

            } else {
             val currentUser = auth.currentUser
                currentUser?. let { user->
                    //Generate unique key for notes
                    val noteKey = databaseReference.child("users").
                    child(user.uid).child("notes").push().key

                    //note item instance
                    val noteItem = NoteItem(tittle,description,noteKey ?: "")
                  if (noteKey!= null)
                      //add notes to user note
                      databaseReference.child("users").child(user.uid).
                      child("notes").child(noteKey).setValue(noteItem)
                          .addOnCompleteListener { task->
                              if (task.isSuccessful){
                                   Toast.makeText(this,"Note saved Successfuly ", Toast.LENGTH_SHORT).show()
                              finish()
                              }
                              else{
                                   Toast.makeText(this,"Failed to save Note ",Toast.LENGTH_SHORT).show()
                              }
                          }
                }

            }

        }


    }
}