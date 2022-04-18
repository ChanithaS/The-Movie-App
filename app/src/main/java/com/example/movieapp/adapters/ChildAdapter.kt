package com.example.movieapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.R
import com.example.movieapp.data.Movie

class ChildAdapter(val childData: List<Movie>) : RecyclerView.Adapter<ChildAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.child_item, parent, false)
        return ViewHolder(inflater)
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val title: TextView = view.findViewById(R.id.movieTitle)
        val info: TextView = view.findViewById(R.id.otherInfo)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val childItem = childData[position]

        holder.title.text = childItem.Title
        holder.info.text = childItem.Genre
    }

    override fun getItemCount(): Int {
        return childData.size
    }
}