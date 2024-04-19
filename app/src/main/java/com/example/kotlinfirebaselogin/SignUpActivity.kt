package com.example.kotlinfirebaselogin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.example.kotlinfirebaselogin.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {
    private val binding: ActivitySignUpBinding by lazy {
        ActivitySignUpBinding.inflate(layoutInflater)
    }

    private  lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //initialize firebase auth

        auth = FirebaseAuth.getInstance()


        binding.tvlogin.setOnClickListener {
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        }

        binding.btnSignup.setOnClickListener {

            //get text from edit text
            val email :String = binding.tvemail2.text.toString()
            val userName :String = binding.tvname2.text.toString()
            val password :String = binding.password2.text.toString()
            val repeatPassword :String = binding.repassword.text.toString()


             //check if any feild is blank
            if (email.isEmpty()){
                binding.tvemail2.setError("Required")
            }
            if (password.isEmpty()){
                binding.password2.setError("Required")

            }
            if (userName.isEmpty()){
                binding.tvname2.setError("Required")
            }
            if (repeatPassword.isEmpty()){
                binding.repassword.setError("Required")

            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

                binding.tvemail2.setError("Invalid Email Format")

            }
            else if (password !=repeatPassword){
                Toast.makeText(this,"Both passwords should be same",Toast.LENGTH_SHORT).show()
            }
            else{
                auth. createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful){
                            Toast.makeText(this,"User Successfuly Registered",Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this,LoginActivity::class.java))
                            finish()
                        }
                        else{
                            Toast.makeText(this,"Registeration Failed: ${task.exception?.message}",Toast.LENGTH_SHORT).show()
                        }

                    }
            }




        }
    }
}