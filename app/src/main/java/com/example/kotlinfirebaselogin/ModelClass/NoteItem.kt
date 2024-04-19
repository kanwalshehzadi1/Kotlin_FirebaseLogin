package com.example.kotlinfirebaselogin.ModelClass

data class NoteItem(val tittle: String, val description: String , val noteId : String){
    constructor(): this("", "" , "")
}

