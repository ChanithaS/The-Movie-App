package com.example.movieapp.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.R
import com.example.movieapp.data.ActorWithMovies

class ParentAdapter(private val activity: Activity, private val parentData: List<ActorWithMovies>) : RecyclerView.Adapter<ParentAdapter.ViewHolder>(){

    /**
     * inflate the viewHolder with the list_item xml
     * @param parent
     * @param viewType
     * @return = the view holder which refer the xml
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ViewHolder(inflater)
    }

    /**
     * @constructor
     * finds the textViews in xml inflated as an recycleView item
     * @param view
     */
    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val actorName: TextView = view.findViewById(R.id.actorNameText)
        val nestedView: RecyclerView = view.findViewById(R.id.nestedView)
    }

    /**
     * goes through the parent items arraylist which has the actor name
     * @param holder
     * @param position
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //position int act as a for loop and inflate one by one in the arraylist
        val parentItem = parentData[position]
        //setting the actor name
        holder.actorName.text = parentItem.actor.actor_name

        //child recycleView ..as one actor may have many movies attended
        //passing the arraylist of movies inside the parent item to the child arraylist
        val childAdapter = ChildAdapter(parentItem.movieAll)
        val linearLayoutManager = LinearLayoutManager(activity)
        holder.nestedView.layoutManager = linearLayoutManager
        holder.nestedView.adapter = childAdapter
    }

    /**
     * @return length of the arraylist of actors
     */
    override fun getItemCount(): Int {
        println(parentData.size)
        return parentData.size
    }
}

//reference
//https://www.youtube.com/watch?v=77XZcP_1n2g
//https://www.youtube.com/watch?v=aK9tOipNm0o
//https://www.geeksforgeeks.org/android-recyclerview-in-kotlin/