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
    private lateinit var movies: List<ActorWithMovies>
    lateinit var notFoundImg : ImageView
    var name: String = ""
    lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_actor)
        supportActionBar?.hide()

        val actorName = findViewById<SearchView>(R.id.NameEditText)
        val searchBtn = findViewById<Button>(R.id.searchBtn)
        recyclerView = findViewById(R.id.parentRecycleView)
        notFoundImg = findViewById(R.id.actorNotFound)
        notFoundImg.visibility = View.GONE

        val db = Room.databaseBuilder(this, MovieDatabase::class.java, "mydatabase").build()
        movieDao = db.movieDao()

        searchBtn.setOnClickListener{
            name = actorName.query.toString()
            searchActor(name)
        }

    }

    private fun searchActor(name: String) {
        runBlocking {
            launch {
                withContext(Dispatchers.IO) {
                    // run the code of the coroutine in a new thread
                    movies = movieDao.getActorMovies(name)
                }
            }
        }
        if (movies.isEmpty()){
            notFoundImg.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        }else{
            recyclerView.visibility = View.VISIBLE
            notFoundImg.visibility = View.GONE
            val parentAdapter = ParentAdapter(this, movies)
            val linearLayoutManager = LinearLayoutManager(this)
            recyclerView.layoutManager = linearLayoutManager
            recyclerView.adapter = parentAdapter
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("name", name)

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        name = savedInstanceState.getString("name")!!
        if (name!= ""){
            searchActor(name)
        }
    }

}