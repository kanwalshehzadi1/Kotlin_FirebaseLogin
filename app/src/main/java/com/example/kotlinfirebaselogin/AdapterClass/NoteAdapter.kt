package com.example.kotlinfirebaselogin.AdapterClass

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinfirebaselogin.ModelClass.NoteItem
import com.example.kotlinfirebaselogin.databinding.NotesItemBinding

class NoteAdapter (private val notes : List<NoteItem>, private val itemClickListener: OnItemClickListener):
    RecyclerView.Adapter<NoteAdapter.NoteViewHolder>(){
   interface OnItemClickListener{
       fun onDeleteClick(noteId:String)
       fun onUpdateClick(noteId:String , tittle:String , description:String)

   }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = NotesItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return NoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]
        holder.bind(note)
        holder.binding.btnDelete.setOnClickListener {
            itemClickListener.onDeleteClick(note.noteId)
        }
        holder.binding.btnUpdate.setOnClickListener {
            itemClickListener.onUpdateClick(note.noteId,note.tittle,note.description)
        }
    }

    override fun getItemCount(): Int {
        return notes.size

    }
    class NoteViewHolder(val binding: NotesItemBinding) :RecyclerView.ViewHolder(binding.root) {
        fun bind(note: NoteItem) {

          binding.tvtitle.text = note.tittle
            binding.tvdescri.text = note.description
        }

    }
}