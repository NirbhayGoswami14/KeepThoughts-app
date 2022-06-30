package com.ncode.keepthoughts.view.activity

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.ncode.keepthoughts.dao.Notes
import com.ncode.keepthoughts.database.DataBase
import com.ncode.keepthoughts.databinding.ActivityCreateNoteBinding
import com.ncode.keepthoughts.repository.NoteRepository
import com.ncode.keepthoughts.viewmodel.NotesViewModel
import com.ncode.keepthoughts.viewmodel.NotesViewModelFactory
import java.text.SimpleDateFormat
import java.util.*

class CreateNoteActivity : AppCompatActivity() {
   private lateinit var binding:ActivityCreateNoteBinding
   private lateinit var database:DataBase
   private lateinit var notesViewModel: NotesViewModel
   private val context:Context=this@CreateNoteActivity
   private var timestamp:String?=""
   private val TAG="CreateNoteActivity"

   override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityCreateNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

       database= DataBase.getDatabase(context)
       val repo=NoteRepository(database.getNotesDao())

        binding.linearBack.setOnClickListener(View.OnClickListener {
            onBackPressed()
        })

       notesViewModel= ViewModelProvider(this,NotesViewModelFactory(repo))[NotesViewModel::class.java]
       Log.d(TAG, "onCreate:==>"+getTimeStamp())

       binding.btnSave.setOnClickListener {
           if(binding.etNotes.text.toString()!="")
               {
                   createNote()
               }
               else if (binding.etTitle.text.toString()!="")
               {
                   createNote()
               }
               else
               {
                   Snackbar.make(binding.root,"Can't Save Empty Notes",Snackbar.LENGTH_SHORT).show()
               }
       }

   }


    private fun getTimeStamp(): String {

        return SimpleDateFormat("MMMM dd yyyy hh:mm a", Locale.getDefault()).format(Date())
    }

    override fun onBackPressed() {

        if(binding.etTitle.text.toString() != "" || binding.etNotes.text.toString()!="")
        {
            alertDialog()
        }
        else
        {

            super.onBackPressed()
        }
    }

    private fun alertDialog()
    {
        var dialog=AlertDialog.Builder(context)
        dialog.setCancelable(false)
        dialog.setTitle("Keep Thoughts")
        dialog.setMessage("Do you Want to Save Changes ? ")
        dialog.setPositiveButton("Save", DialogInterface.OnClickListener { dialogInterface, i -> createNote()})
        dialog.setNegativeButton("Don't Save", DialogInterface.OnClickListener { dialogInterface, i ->
            finish()

         })
        dialog.create()
        dialog.show()
    }

    private fun createNote()
    {
        notesViewModel.createNote(Notes(0,binding.etTitle.text.toString(),binding.etNotes.text.toString(),getTimeStamp()))
        finish()
    }
}