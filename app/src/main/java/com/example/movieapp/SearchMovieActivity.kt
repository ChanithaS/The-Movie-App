package com.example.movieapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.*
import androidx.room.Room
import com.example.movieapp.data.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class SearchMovieActivity : AppCompatActivity() {
    private lateinit var movieName: SearchView
    private lateinit var infoText: TextView
    private lateinit var movieDao: MovieDao
    private lateinit var newMovie: Movie
    private lateinit var listOfActors : List<String>
    private lateinit var titleMovie : String
    lateinit var notFound : ImageView
    private var isThere: Boolean = false
    var name: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_movie)
        supportActionBar?.hide()

        val db = Room.databaseBuilder(this, MovieDatabase::class.java, "mydatabase").build()
        movieDao = db.movieDao()

        val retrieve = findViewById<Button>(R.id.retrieveMovieBtn)
        val save = findViewById<Button>(R.id.saveBtn)
        movieName = findViewById(R.id.NameEditText)
        infoText = findViewById(R.id.retrievedInfoText)
        notFound = findViewById(R.id.notFound)
        notFound.visibility = View.GONE
        infoText.visibility = View.GONE

        retrieve.setOnClickListener{
            name = movieName.query.toString()
            searchData(name)
        }

        save.setOnClickListener{
            runBlocking {
                launch {
                    // run the code of the coroutine in a new thread
                    withContext(Dispatchers.IO) {
                        saveToDatabase()
                    }
                }
            }
        }
    }

    private fun searchData(name: String) {
        // collecting all the JSON string
        var stb = StringBuilder("")
        val urlString = "https://www.omdbapi.com/?t=${name}&apikey=1ef0b74d";
        val url = URL(urlString)
        val con: HttpURLConnection = url.openConnection() as HttpURLConnection

        var data:String = ""
        runBlocking {
            launch {
                // run the code of the coroutine in a new thread
                withContext(Dispatchers.IO) {
                    var bf = BufferedReader(InputStreamReader(con.inputStream))
                    var line: String? = bf.readLine()
                    while (line != null) {
                        stb.append(line + "\n")
                        line = bf.readLine()
                    }

                    data = parseJSON(stb)

                    runOnUiThread{
                        infoText.text = data
                    }
                }
            }
        }
    }
    private suspend fun saveToDatabase(){
        if (isThere){
            movieDao.addMovie(newMovie)
            for (names in listOfActors){
                val actor = Actor(names)
                movieDao.addActor(actor)

                val actorWithMovie = ActorMovieCrossRef(titleMovie, names)
                movieDao.addActorMovie(actorWithMovie)
            }
        }
    }

    private fun parseJSON(stb: StringBuilder): String {
        val json = JSONObject(stb.toString()) //convert string to JSON object
        isThere = json["Response"] == "True"
        if (isThere){
            runOnUiThread {
                infoText.visibility = View.VISIBLE
                notFound.visibility = View.GONE
            }
            var info:String
            val title = json["Title"].toString()
            val year = json["Year"].toString()
            val rated = json["Rated"].toString()
            val released = json["Released"].toString()
            val runtime = json["Runtime"].toString()
            val genre = json["Genre"].toString()
            val director = json["Director"].toString()
            val writer = json["Writer"].toString()
            val actors = json["Actors"].toString()
            val plot = json["Plot"].toString()

            info = "Title: $title\n, Year: $year\n, Rated: $rated\n, Released: $released\n, Runtime: $runtime, Genre: $genre\n, Director: $director\n, Writer: $writer\n, Actors: $actors\n, Plot: $plot"
            newMovie = Movie(title,year, rated,released,runtime,genre,director,writer,actors,plot)
            titleMovie = title

            listOfActors = listOf(*actors.split(",").toTypedArray())  //https://www.techiedelight.com/convert-string-to-list-kotlin/

            return info
        }else{
            runOnUiThread {
                notFound.visibility = View.VISIBLE
                infoText.visibility = View.GONE
                //code that runs in main
            }
            return ""
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("name", name)

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        name = savedInstanceState.getString("name")!!
        if (name != ""){
            searchData(name)
        }

    }
}