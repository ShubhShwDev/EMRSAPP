package com.maxi.dogapi.roomdb

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.NO_POSITION
import com.maxi.dogapi.R

class NoteAdapter(private val onItemClickListener:ClickListen)
    : ListAdapter<Note, NoteAdapter.NoteHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.note_item, parent,
            false)
        return NoteHolder(itemView)
    }

    override fun onBindViewHolder(holder: NoteHolder, position: Int) {
        with(getItem(position)) {
            holder.tvTitle.text = officer_name
            holder.tvDescription.text = observation.toString()
            holder.tvPriority.text = priority.toString()
        }

        holder.itemView.setOnClickListener {
            if(holder.adapterPosition != NO_POSITION)
                onItemClickListener.onClickFun("0", holder.adapterPosition,getNoteAt(holder.adapterPosition) )
        }

    }

    fun getNoteAt(position: Int) = getItem(position)


    inner class NoteHolder(iv: View) : RecyclerView.ViewHolder(iv) {

        val tvTitle: TextView = itemView.findViewById(R.id.text_view_title)
        val tvDescription: TextView = itemView.findViewById(R.id.text_view_description)
        val tvPriority: TextView = itemView.findViewById(R.id.text_view_priority)

        init {
        }

    }
}

private val diffCallback = object : DiffUtil.ItemCallback<Note>() {
    override fun areItemsTheSame(oldItem: Note, newItem: Note) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Note, newItem: Note) =
        oldItem.officer_name == newItem.officer_name
                && oldItem.designation == newItem.designation
                && oldItem.priority == newItem.priority
}