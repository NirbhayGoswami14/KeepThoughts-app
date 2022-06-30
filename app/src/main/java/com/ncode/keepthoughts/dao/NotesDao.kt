package com.ncode.keepthoughts.dao

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NotesDao {

    @Insert
    suspend fun createNote(note:Notes)

    @Update
    suspend fun updateNote(note: Notes)

    @Query("DELETE FROM notes WHERE noteId=:id")
    suspend fun deleteNotes(id:Int)

    @Query("SELECT * FROM notes")
     fun getNotes():LiveData<List<Notes>>

}