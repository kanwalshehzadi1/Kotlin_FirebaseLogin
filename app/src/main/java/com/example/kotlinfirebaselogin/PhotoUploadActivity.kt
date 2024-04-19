package com.example.kotlinfirebaselogin

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.kotlinfirebaselogin.databinding.ActivityPhotoUploadBinding
import com.google.firebase.Firebase
import com.google.firebase.database.database
import com.google.firebase.storage.storage
import com.squareup.picasso.Picasso

class PhotoUploadActivity : AppCompatActivity() {
    private lateinit var binding : ActivityPhotoUploadBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityPhotoUploadBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnVdeoactivity.setOnClickListener {
            startActivity(Intent(this,VideoActivity::class.java))
            finish()
        }
        binding.btnUpload.setOnClickListener {
            val intent = Intent()
            intent.action= Intent.ACTION_PICK
            intent.type="image/*"
            imageLauncher.launch(intent)
        }
    }
    val imageLauncher=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode== Activity.RESULT_OK)
        {
            if (it.data!=null){
                  val ref=Firebase.storage.reference.child("Photo/"+System.currentTimeMillis()+"."+getFileType(it.data!!.data))
                ref.putFile(it.data!!.data!!).addOnSuccessListener {
                    ref.downloadUrl.addOnSuccessListener {

                      Firebase.database.reference.child("Photo").push().setValue(it.toString())

                       /* binding.btnUpload.setImageURI(it)*/
                         Toast.makeText(this@PhotoUploadActivity,"Photo Uploaded ", Toast.LENGTH_LONG).show()
                        Picasso.get().load(it.toString()).into(binding.imageView2);
                    }
                }
            }
        }
    }

    private fun getFileType(data: Uri?): String? {
        val r = contentResolver
        val mimeType=MimeTypeMap.getSingleton()
        return mimeType.getMimeTypeFromExtension(r.getType(data!!))
    }
}