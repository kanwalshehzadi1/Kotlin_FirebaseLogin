package com.example.kotlinfirebaselogin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.example.kotlinfirebaselogin.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginActivity : AppCompatActivity() {
    private  val binding:ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }
    private lateinit var auth:FirebaseAuth

    override fun onStart() {
        super.onStart()

        val currentUser : FirebaseUser? = auth.currentUser
        if (currentUser != null){
            startActivity(Intent(this,NotesActivity::class.java))
            finish()

        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //initialize
        auth = FirebaseAuth.getInstance()




        binding.tvsignup.setOnClickListener {
            startActivity(Intent(this,SignUpActivity::class.java))
            finish()
        }

        binding.btnLogin.setOnClickListener {

            //get text from edit text
            val password:String = binding.tvpassword.text.toString()
            val email:String = binding.tvemail.text.toString()

            if (email.isEmpty()){
                binding.tvemail.setError("Required")
            }
            if (password.isEmpty()){
                binding.tvpassword.setError("Required")

            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

                binding.tvemail.setError("Invalid Email Format")

            }

            else{
                auth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener (this) { task ->
                        if (task.isSuccessful){
                            Toast.makeText(this," Login Successful",Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        }

                        else{
                            Toast.makeText(this,"Login Failed: ${task.exception?.message}",Toast.LENGTH_SHORT).show()
                        }
                    }


            }


        }
    }
}