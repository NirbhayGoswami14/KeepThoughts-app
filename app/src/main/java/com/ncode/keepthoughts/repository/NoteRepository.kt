package com.ncode.keepthoughts.repository

import androidx.lifecycle.LiveData
import com.ncode.keepthoughts.dao.Notes
import com.ncode.keepthoughts.dao.NotesDao

class NoteRepository(var notesDao: NotesDao)
{
        fun getNotes():LiveData<List<Notes>>
        {
            return notesDao.getNotes()
        }

        suspend fun createNotes(notes: Notes)
        {
            notesDao.createNote(notes)
        }

        suspend fun updateNotes(notes: Notes)
        {
            notesDao.updateNote(notes)
        }

        suspend fun deleteNotes(id:Int)
        {
            notesDao.deleteNotes(id)
        }

}