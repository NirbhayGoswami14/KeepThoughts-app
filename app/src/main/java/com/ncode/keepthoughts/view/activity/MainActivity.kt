package com.ncode.keepthoughts.view.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Filter
import android.widget.ImageView
import android.widget.PopupMenu
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.ncode.keepthoughts.R
import com.ncode.keepthoughts.adapter.NotesAdapter
import com.ncode.keepthoughts.dao.Notes
import com.ncode.keepthoughts.database.DataBase
import com.ncode.keepthoughts.databinding.ActivityMainBinding
import com.ncode.keepthoughts.repository.NoteRepository
import com.ncode.keepthoughts.util.Constant
import com.ncode.keepthoughts.viewmodel.NotesViewModel
import com.ncode.keepthoughts.viewmodel.NotesViewModelFactory

class MainActivity : AppCompatActivity(), NotesAdapter.ClickListener,
    NotesAdapter.LongClickListener {
   lateinit var binding:ActivityMainBinding
   lateinit var adapter:NotesAdapter
   var context:Context=this@MainActivity
   private val TAG="MainActivity"
   private var noteList:ArrayList<Notes>?=null
   private lateinit var notesViewModel:NotesViewModel

   override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

      // adapter= NotesAdapter(context,ArrayList())

       binding.btnCreateNote.setOnClickListener(View.OnClickListener {
           startActivity(Intent(this@MainActivity,CreateNoteActivity::class.java))
       })

       binding.linearSearch.setOnClickListener {

           binding.search.performClick()
           binding.search.visibility=View.VISIBLE
           binding.search.isIconified=false
           binding.linearSearch.visibility=View.INVISIBLE

       }

        getNotes()

        var closeButton=binding.search.findViewById<ImageView>(androidx.appcompat.R.id.search_close_btn)

       closeButton.setOnClickListener {
           binding.search.visibility=View.INVISIBLE
           binding.search.isIconified=true
           binding.linearSearch.visibility=View.VISIBLE

                  }


       binding.search.setOnQueryTextListener(object:SearchView.OnQueryTextListener{

           override fun onQueryTextSubmit(query: String?): Boolean {

               return false
           }

           override fun onQueryTextChange(newText: String?): Boolean {
             /*  adapter.filter(newText!!)

               if(newText.length==0)
               {
                   getNotes()
               }*/
               adapter.filter!!.filter(newText,object :Filter.FilterListener{
                   override fun onFilterComplete(p0: Int) {
                       if(p0==0)
                       {
                           Log.d(TAG, "onFilterComplete: ====>")

                       }
                   }
               })
               if(newText!!.length==0)
               {
                   getNotes()
               }
               return false
           }
       })



    }



   private fun setRecyclerView(noteList: ArrayList<Notes>?)
    {
        adapter= NotesAdapter(context,noteList)
        binding.rcNotes.layoutManager=StaggeredGridLayoutManager(2,LinearLayoutManager.VERTICAL)
        binding.rcNotes.adapter=adapter
        adapter.setOnCLickListener(this)
        adapter.setOnLongClickListener(this)

    }

    override fun onClick(notes: Notes) {
        startActivity(Intent(context,EditNoteActivity::class.java).putExtra(Constant.NOTE_ID,notes.noteId)
            .putExtra(Constant.NOTE_TITLE,notes.noteTitle)
            .putExtra(Constant.NOTES,notes.notes)
            .putExtra(Constant.TIME_STAMP,notes.timestamp))
    }
    private fun getNotes()
    {
        var noteRepo=NoteRepository(DataBase.getDatabase(context).getNotesDao())
        notesViewModel=ViewModelProvider(this,NotesViewModelFactory(noteRepo))[NotesViewModel::class.java]

        notesViewModel.getNotes().observe(this, Observer {
            noteList= it as ArrayList<Notes>?
            Log.d(TAG, "onCreate:${it.size}")

            if(noteList!!.size!=0)
            {
                setRecyclerView(noteList)
                binding.rcNotes.visibility=View.VISIBLE
                binding.imgSticker.visibility=View.INVISIBLE

            }
            else
            {
                binding.rcNotes.visibility=View.GONE
                binding.imgSticker.visibility=View.VISIBLE
            }
        })

    }

    override fun longClick(noteId: Int, itemView: View) {

        var popMenu=PopupMenu(context,itemView)
        popMenu.menuInflater.inflate(R.menu.popmenu_menu,popMenu.menu)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            popMenu.gravity=Gravity.TOP
        }
        popMenu.setOnMenuItemClickListener {
            when(it.itemId)
            {
                R.id.del->{

                    notesViewModel.deleteNotes(noteId)

                }
            }

            true
        }

        popMenu.show()

    }


}