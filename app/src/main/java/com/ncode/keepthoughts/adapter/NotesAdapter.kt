package com.ncode.keepthoughts.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.ncode.keepthoughts.dao.Notes
import com.ncode.keepthoughts.databinding.NotesAdapterBinding
import com.ncode.keepthoughts.util.Colors
import java.util.*
import kotlin.collections.ArrayList

class NotesAdapter(private val context: Context, var noteList: ArrayList<Notes>?):RecyclerView.Adapter<NotesAdapter.HOLDER>(),Filterable {
   private lateinit var clickListener:ClickListener
   private lateinit var longClickListener: LongClickListener
   private  var noteListCopy:ArrayList<Notes> = noteList!!



    class HOLDER(inflate: NotesAdapterBinding):RecyclerView.ViewHolder(inflate.root)
    {
        var binding:NotesAdapterBinding=inflate

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HOLDER {

        return HOLDER(NotesAdapterBinding.inflate(LayoutInflater.from(context),parent,false))
    }

    override fun onBindViewHolder(holder: HOLDER, position: Int) {
        holder.binding.cardview.setCardBackgroundColor(Color.parseColor(Colors.getColor()))
        holder.binding.txtTitle.text=noteList!![position].noteTitle
        holder.binding.txtNotes.text=noteList!![position].notes
        holder.binding.txtTimeStamp.text=noteList!![position].timestamp

        holder.itemView.setOnClickListener {

            clickListener.onClick(Notes(noteList!![position].noteId,noteList!![position].noteTitle,noteList!![position].notes,noteList!![position].timestamp))
        }

        holder.itemView.setOnLongClickListener(View.OnLongClickListener {
            longClickListener.longClick(noteList!![position].noteId,holder.itemView)
            true })

    }

    override fun getItemCount(): Int {

        return noteList!!.size
    }


    fun setOnCLickListener(clickListener: ClickListener)
    {
        this.clickListener=clickListener
    }
    fun setOnLongClickListener(longClickListener: LongClickListener)
    {
        this.longClickListener=longClickListener
    }

    interface ClickListener
    {
        fun onClick(notes: Notes)

    }
    interface LongClickListener
    {
        fun longClick(noteId: Int, itemView: View)
    }

    override fun getFilter(): Filter {
        var filter=object:Filter(){
            override fun performFiltering(p0: CharSequence?): FilterResults {
                var filterList:ArrayList<Notes> = ArrayList()
                if(TextUtils.isEmpty(p0))
                {
                    filterList.addAll(noteListCopy)
                }else {
                    val filterPattern = p0.toString().lowercase(Locale.getDefault())
                    for (item in noteListCopy) {
                        if (item.noteTitle.lowercase(Locale.getDefault()).contains(filterPattern))
                        {
                            filterList.add(item)
                        }
                        else
                        {
                            filterList.remove(item)
                        }
                    }
                }
                val results = FilterResults()
                results.values = filterList
                return results
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun publishResults(p0: CharSequence?, p1: FilterResults?)
            {
                noteList!!.clear()
                noteList!!.addAll(p1!!.values as Collection<Notes>)
                notifyDataSetChanged()
            }

        }
        return filter
    }


  /*  @SuppressLint("NotifyDataSetChanged")
    fun filter(query:String)
    {
        query.lowercase(Locale.getDefault())
        noteList!!.clear()
        if(query.lowercase(Locale.getDefault()).length==0)
        {
            Log.d("", "filter:===>")
            Log.d("", "filter:===>${noteList!!.size}")
            Log.d("", "filter:===>copy${noteListCopy.size}")
                noteList!!.addAll(noteListCopy)
        }
        else
        {
                for(n in noteListCopy)
                {
                    if(n.noteTitle.lowercase(Locale.getDefault()).contains(query))
                    {
                                noteList!!.add(n)
                    }
                }
        }
        notifyDataSetChanged()
    }*/


}