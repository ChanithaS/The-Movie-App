package com.example.movieapp

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.movieapp.adapters.ParentAdapter
import com.example.movieapp.data.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class SearchActorActivity : AppCompatActivity() {
    private lateinit var movieDao: MovieDao
    private lateinit var movies: List<ActorWithMovies>      //list with the cross Refer for all the movies of actors
    lateinit var notFoundImg : ImageView                    //image to display when not found
    var name: String = ""                                   //user input
    lateinit var recyclerView: RecyclerView                 //recycle view for displaying the data

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_actor)
        supportActionBar?.hide()

        //initializing the elements
        val actorName = findViewById<SearchView>(R.id.NameEditText)
        val searchBtn = findViewById<Button>(R.id.searchBtn)
        recyclerView = findViewById(R.id.parentRecycleView)
        notFoundImg = findViewById(R.id.actorNotFound)
        //making the visibility of not found image disapear
        notFoundImg.visibility = View.GONE

        //initializing the db
        val db = Room.databaseBuilder(this, MovieDatabase::class.java, "mydatabase").build()
        movieDao = db.movieDao()

        //calling the function on click
        searchBtn.setOnClickListener{
            name = actorName.query.toString()
            searchActor(name)
        }
    }

    /**
     * calling the many to many relationship of actors and movies
     * then looking for all the movies the actor has attended in the recycleView adapter
     * @param name
     */
    private fun searchActor(name: String) {
        runBlocking {
            launch {
                withContext(Dispatchers.IO) {
                    // run the code of the coroutine in a new thread
                    //getting the data from database query
                    movies = movieDao.getActorMovies(name)
                }
            }
        }
        //checking if the array is empty meaning no movie is found for that name
        if (movies.isEmpty()){
            //setting the image to visible and recycleView to disappear
            notFoundImg.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        }else{
            //setting image to disappear and recycle view to visible
            recyclerView.visibility = View.VISIBLE
            notFoundImg.visibility = View.GONE

            //making a new parentAdapter with the movie array passed
            val parentAdapter = ParentAdapter(this, movies)
            //setting the recycle view layout to display in a linear layout
            val linearLayoutManager = LinearLayoutManager(this)
            recyclerView.layoutManager = linearLayoutManager
            recyclerView.adapter = parentAdapter
        }
    }

    /**
     * saving the values
     * @param outState
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("name", name)
    }

    /**
     * restore the values after orientation
     * @param savedInstanceState
     */
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        name = savedInstanceState.getString("name")!!

        //checking if the input is null.. if not only calling the function
        if (name!= ""){
            searchActor(name)
        }
    }

}