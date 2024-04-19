package com.example.kotlinfirebaselogin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinfirebaselogin.AdapterClass.NoteAdapter
import com.example.kotlinfirebaselogin.ModelClass.NoteItem
import com.example.kotlinfirebaselogin.databinding.ActivityAllNotesBinding
import com.example.kotlinfirebaselogin.databinding.DialougeUpdateNoteBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AllNotes : AppCompatActivity() , NoteAdapter.OnItemClickListener{

    private val binding: ActivityAllNotesBinding by lazy {
        ActivityAllNotesBinding.inflate(layoutInflater)
    }

    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        recyclerView = binding.notesRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        //Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()

        val currentUser = auth.currentUser
        currentUser?.let { user ->
            val noteReference = databaseReference.child("users").
            child(user.uid).child("notes")
            noteReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    val noteList = mutableListOf<NoteItem>()
                    for (noteSnapshot in snapshot.children) {
                        val note = noteSnapshot.getValue(NoteItem::class.java)
                        note?.let {
                            noteList.add(it)
                        }
                    }
                    noteList.reverse()
                    val adapter = NoteAdapter(noteList,this@AllNotes)
                    recyclerView.adapter = adapter

                    //Add log statement to check if data is retrived or not
                    Log.d("AllNotes", "Data retrived: $noteList")


                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
        }
    }


    //Delete code
    override fun onDeleteClick(noteId: String) {


        val currentUser = auth.currentUser
        currentUser?.let {user->
            val noteReference = databaseReference.child("users").child(user.uid).
            child("notes")
            noteReference.child(noteId).removeValue()
        }

    }


    //update Code
    override fun onUpdateClick(noteId: String, currentTittle:String, currentDescription:String) {
        val dialogBinding = DialougeUpdateNoteBinding.inflate(LayoutInflater.from(this))
        val dialog = AlertDialog.Builder(this).setView(dialogBinding.root)
            .setTitle("Update Notes")
            .setPositiveButton("Update") {dialog,_->
                val newTittle = dialogBinding.updateTitle.text.toString()
                val newDescription = dialogBinding.updateDescri.text.toString()
                updateNoteDatabase(noteId,newTittle,newDescription)
                dialog.dismiss()
            }
            .setNegativeButton("Cancel"){dialog,_->
                dialog.dismiss()
            }
            .create()
        dialogBinding.updateTitle.setText(currentTittle)
        dialogBinding.updateDescri.setText(currentDescription)

        dialog.show()





    }

    private fun updateNoteDatabase(noteId: String, newTittle: String, newDescription: String) {
        val currentUser = auth.currentUser
        currentUser?.let{user->
            val noteReference = databaseReference.child("users").child(user.uid).
            child("notes")
            val updateNote= NoteItem(newTittle, newDescription,noteId)
            noteReference.child(noteId).setValue(updateNote)
                .addOnCompleteListener { task->
                    if(task.isSuccessful){
                        Toast.makeText(this," Note Updated",Toast.LENGTH_SHORT).show()
                    }
                    else{
                        Toast.makeText(this," Updation Failed",Toast.LENGTH_SHORT).show()
                    }
                }
        }



    }
}
