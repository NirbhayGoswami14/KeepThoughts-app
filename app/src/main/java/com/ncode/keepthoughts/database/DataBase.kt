package com.ncode.keepthoughts.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ncode.keepthoughts.dao.Notes
import com.ncode.keepthoughts.dao.NotesDao

@Database(entities = [Notes::class], version = 1)
 abstract class DataBase : RoomDatabase() {

        abstract fun getNotesDao():NotesDao

        companion object
        {
            var dataBase:DataBase?=null
            fun getDatabase(context: Context):DataBase
            {
                if(dataBase==null)
                {
                    synchronized(this)
                    {
                        dataBase=Room.databaseBuilder(context,DataBase::class.java,"keepThoughts").build()
                    }
                }

                return dataBase!!
            }

        }
}