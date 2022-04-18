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

//https://www.youtube.com/watch?v=77XZcP_1n2g
//https://www.youtube.com/watch?v=aK9tOipNm0o
//https://www.geeksforgeeks.org/android-recyclerview-in-kotlin/

class ParentAdapter(private val activity: Activity, private val parentData: List<ActorWithMovies>) : RecyclerView.Adapter<ParentAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ViewHolder(inflater)
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val actorName: TextView = view.findViewById(R.id.actorNameText)
        val nestedView: RecyclerView = view.findViewById(R.id.nestedView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val parentItem = parentData[position]
        holder.actorName.text = parentItem.actor.actor_name

        val childAdapter = ChildAdapter(parentItem.movieAll)
        val linearLayoutManager = LinearLayoutManager(activity)
        holder.nestedView.layoutManager = linearLayoutManager
        holder.nestedView.adapter = childAdapter
    }

    override fun getItemCount(): Int {
        println(parentData.size)
        return parentData.size
    }
}