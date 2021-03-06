package com.example.movieapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.R
import com.example.movieapp.data.Movie

class ChildAdapter(private val childData: List<Movie>) : RecyclerView.Adapter<ChildAdapter.ViewHolder>() {

    /**
     * inflate the viewHolder with the child_item xml
     * @param parent
     * @param viewType
     * @return
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.child_item, parent, false)
        return ViewHolder(inflater)
    }

    /**
     * @constructor
     * TODO
     * finds the textViews in xml inflated as an recycleView item
     * @param view
     */
    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val title: TextView = view.findViewById(R.id.movieTitle)
        val info: TextView = view.findViewById(R.id.otherInfo)
    }

    /**
     * this goes through all the movies attended by the specific actor and assign them to the textViews
     * @param holder
     * @param position = current int position of the movies array
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val childItem = childData[position]

        holder.title.text = childItem.Title
        holder.info.text = childItem.Genre
    }

    /**
     * @return arraylist size of the movies
     */
    override fun getItemCount(): Int {
        return childData.size
    }
}