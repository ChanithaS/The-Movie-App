package com.example.movieapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.R
import org.json.JSONArray
import org.json.JSONObject


class OnlineMoviesAdapter( private val movieData: JSONArray) : RecyclerView.Adapter<OnlineMoviesAdapter.ViewHolder>(){

    /**
     * inflate the viewHolder with the movie_item xml
     *  for all the movies retrieved with similar name from online
     * @param parent
     * @param viewType
     * @return
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false)
        return ViewHolder(inflater)
    }

    /**
     * @constructor
     * initializes the textViews in xml
     * @param view
     */
    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val movieTitle: TextView = view.findViewById(R.id.titleMovie)
        val movieYear: TextView = view.findViewById(R.id.yearMovie)
        val imdbNo: TextView = view.findViewById(R.id.imdbText)
    }

    /**
     * read the JSONObject array one by one and extract the data and assign to testViews
     * @param holder
     * @param position = current int position of the object array
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //casting JSONArray index object to JSONObject
        val movie: JSONObject = movieData[position] as JSONObject

        //extracting the specific index data
        val title = movie["Title"].toString()
        val year = movie["Year"].toString()
        val imdb = movie["imdbID"].toString()

        //assigning year, imbdID with their title
        val yearText = "Year : $year"
        val imdbText = "imdbID : $imdb"

        //assigning the values to textViews
        holder.movieTitle.text = title
        holder.movieYear.text = yearText
        holder.imdbNo.text = imdbText
    }

    /**
     * @return  JSONArraylist size of the movies
     */
    override fun getItemCount(): Int {
        return movieData.length()
    }
}