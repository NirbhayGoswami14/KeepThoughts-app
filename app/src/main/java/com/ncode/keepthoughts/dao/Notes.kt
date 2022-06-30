package com.ncode.keepthoughts.dao

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Timestamp

@Entity(tableName = "notes")
data class Notes(
    @PrimaryKey(autoGenerate = true)
    var noteId:Int,
    var noteTitle:String,
    var notes:String,
    var timestamp:String
)
