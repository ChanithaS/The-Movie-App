package com.example.movieapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.SearchView
import android.widget.TextView
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

//    lateinit var nam: ActorWithMovies

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_actor)

        val actorName = findViewById<SearchView>(R.id.actorName)
        val searchBtn = findViewById<Button>(R.id.searchBtn)
        val recyclerView = findViewById<RecyclerView>(R.id.parentRecycleView)

        val db = Room.databaseBuilder(this, MovieDatabase::class.java, "mydatabase").build()
        movieDao = db.movieDao()


        searchBtn.setOnClickListener{
            runBlocking {
                launch {
                    withContext(Dispatchers.IO) {
                        // run the code of the coroutine in a new thread
                        movies = movieDao.getActorMovies(actorName.query.toString())
                    }
                }
            }
            val parentAdapter = ParentAdapter(this, movies)
            val linearLayoutManager = LinearLayoutManager(this)
            recyclerView.layoutManager = linearLayoutManager
            recyclerView.adapter = parentAdapter
//            for (u in movies) {
//                val name = u.actor.actor_name
//                for(m in u.movieAll){
////                    text.append("\n ${m.Title}  $name")
//                }
//            }
        }

    }
}