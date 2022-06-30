package com.ncode.keepthoughts.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.ncode.keepthoughts.repository.NoteRepository
import com.ncode.keepthoughts.dao.Notes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class NotesViewModel(var repository: NoteRepository): ViewModel() {

    fun getNotes():LiveData<List<Notes>>
    {
       return repository.getNotes()
    }

     fun createNote(notes: Notes)
    {
        CoroutineScope(IO).launch {

            repository.createNotes(notes)
        }
    }

     fun updateNotes(notes: Notes)
    {
        CoroutineScope(IO).launch {

            repository.updateNotes(notes)
        }
    }

    fun deleteNotes(id:Int)
    {
        CoroutineScope(IO).launch {

            repository.deleteNotes(id)
        }
    }
}