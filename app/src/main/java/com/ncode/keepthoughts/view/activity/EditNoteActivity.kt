package com.ncode.keepthoughts.view.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.ncode.keepthoughts.dao.Notes
import com.ncode.keepthoughts.database.DataBase
import com.ncode.keepthoughts.databinding.ActivityEditeNoteBinding
import com.ncode.keepthoughts.databinding.BottomMenuLayoutBinding
import com.ncode.keepthoughts.repository.NoteRepository
import com.ncode.keepthoughts.util.Constant
import com.ncode.keepthoughts.viewmodel.NotesViewModel
import com.ncode.keepthoughts.viewmodel.NotesViewModelFactory
import java.text.SimpleDateFormat
import java.util.*

class EditNoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditeNoteBinding
    private lateinit var notesViewModel: NotesViewModel
    private lateinit var repository: NoteRepository
    private var  context: Context =this@EditNoteActivity
    private val TAG="EditNoteActivity"
    private var noteId = 0
    private var notes = ""
    private var noteTitle = ""
    private var timestamp = ""


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditeNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.linearBack.setOnClickListener(View.OnClickListener {
            onBackPressed()
        })
        repository= NoteRepository(DataBase.getDatabase(context).getNotesDao())
        notesViewModel=ViewModelProvider(this,NotesViewModelFactory(repository))[NotesViewModel::class.java]

        noteTitle = intent!!.getStringExtra(Constant.NOTE_TITLE).toString()
        notes = intent!!.getStringExtra(Constant.NOTES).toString()
        noteId = intent!!.getIntExtra(Constant.NOTE_ID,0)
        timestamp = intent!!.getStringExtra(Constant.TIME_STAMP).toString()


        binding.etEditTitle.setText(noteTitle)
        binding.etEditNotes.setText(notes)
        binding.txtTimeStamp.text="Edited\t$timestamp"

        binding.linearBack.setOnClickListener {
            onBackPressed()
        }
        binding.linearEdit.setOnClickListener {
            if(binding.etEditTitle.text.toString()=="" || binding.etEditTitle.text.toString()!="" )
            {
                if(binding.etEditNotes.text.toString()!="")
                {

                    editNotes()
                }
                else
                {
                    Snackbar.make(binding.root,"Can't Save Empty Notes", Snackbar.LENGTH_SHORT).show()
                }
            }


        }

        binding.imgMenuDot.setOnClickListener {
                bottomMenu()
        }
    }

    override fun onBackPressed() {
        if(binding.etEditTitle.text.toString()!=noteTitle || binding.etEditNotes.text.toString()!=notes)
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
        var dialog= AlertDialog.Builder(context)
        dialog.setCancelable(false)
        dialog.setTitle("Keep Thoughts")
        dialog.setMessage("Do you Want to Save Changes ? ")
        dialog.setPositiveButton("Save", DialogInterface.OnClickListener { dialogInterface, i -> editNotes()})
        dialog.setNegativeButton("Don't Save", DialogInterface.OnClickListener { dialogInterface, i ->
            dialogInterface.dismiss()
            finish()
        })
        dialog.create()
        dialog.show()
    }


    private  fun editNotes()
    {
        notesViewModel.updateNotes(Notes(noteId,binding.etEditTitle.text.toString(),binding.etEditNotes.text.toString(),getTimeStamp()))
        finish()
    }

    private fun getTimeStamp(): String {

        return SimpleDateFormat("MMMM dd yyyy hh:mm a", Locale.getDefault()).format(Date())
    }

    private fun bottomMenu()
    {
        var menuBinding=BottomMenuLayoutBinding.inflate(layoutInflater)
         var bottomDialog=BottomSheetDialog(context)
        bottomDialog.dismissWithAnimation=true
        bottomDialog.setContentView(menuBinding.root)
        bottomDialog.show()

        menuBinding.txtDelete.setOnClickListener {
            notesViewModel.deleteNotes(noteId)
            finish()
        }
        menuBinding.txtShare.setOnClickListener {

            Log.d(TAG, "bottomMenu:===>")
            var sendIntent=Intent(Intent.ACTION_SEND)
            sendIntent.type = "text/plain"
            sendIntent.putExtra(Intent.EXTRA_TEXT,binding.etEditNotes.text.toString())
            startActivity(Intent.createChooser(sendIntent,null))
        }


    }
}