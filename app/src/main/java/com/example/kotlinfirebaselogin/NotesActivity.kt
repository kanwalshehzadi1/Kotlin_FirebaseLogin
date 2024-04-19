package com.example.kotlinfirebaselogin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.kotlinfirebaselogin.databinding.ActivityMainBinding
import com.example.kotlinfirebaselogin.databinding.ActivityNotesBinding

class NotesActivity : AppCompatActivity() {
    private val binding : ActivityNotesBinding by lazy {
        ActivityNotesBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnCreate.setOnClickListener {
           startActivity(Intent(this,AddNote::class.java))
        }
 binding.openallnotesBtn.setOnClickListener {
           startActivity(Intent(this,AllNotes::class.java))
        }

    }
}