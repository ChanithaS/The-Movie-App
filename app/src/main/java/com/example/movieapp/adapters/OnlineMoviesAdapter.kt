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
import org.json.JSONArray
import org.json.JSONObject


class OnlineMoviesAdapter( private val movieData: JSONArray) : RecyclerView.Adapter<OnlineMoviesAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false)
        return ViewHolder(inflater)
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val movieTitle: TextView = view.findViewById(R.id.titleMovie)
        val movieYear: TextView = view.findViewById(R.id.yearMovie)
        val imdbNo: TextView = view.findViewById(R.id.imdbText)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie: JSONObject = movieData[position] as JSONObject

        val title = movie["Title"].toString()
        val year = movie["Year"].toString()
        val imdb = movie["imdbID"].toString()

        val yearText = "Year : $year"
        val imdbText = "imdbID : $imdb"

        holder.movieTitle.text = title
        holder.movieYear.text = yearText
        holder.imdbNo.text = imdbText
    }

    override fun getItemCount(): Int {
        return movieData.length()
    }
}