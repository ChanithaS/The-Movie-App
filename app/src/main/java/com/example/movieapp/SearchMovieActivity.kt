package com.example.movieapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
    private lateinit var movieName: SearchView          //user input
    private lateinit var infoText: TextView             //display data
    private lateinit var movieDao: MovieDao
    private lateinit var newMovie: Movie                //movie table ref
    private lateinit var listOfActors : List<String>    //all actors
    private lateinit var titleMovie : String            //all titles of movies
    lateinit var notFound : ImageView
    private var isThere: Boolean = false                //check if data is retrieved
    var name: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_movie)
        supportActionBar?.hide()

        //initializing elements
        val db = Room.databaseBuilder(this, MovieDatabase::class.java, "mydatabase").build()
        movieDao = db.movieDao()

        val retrieve = findViewById<Button>(R.id.retrieveMovieBtn)
        val save = findViewById<Button>(R.id.saveBtn)
        movieName = findViewById(R.id.NameEditText)
        infoText = findViewById(R.id.retrievedInfoText)
        notFound = findViewById(R.id.notFound)
        //setting visibility to false at start
        notFound.visibility = View.GONE
        infoText.visibility = View.GONE

        //to get data
        retrieve.setOnClickListener{
            //user input name of movie
            name = movieName.query.toString()
            searchData(name)
        }

        //to save the data on DB
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

    /**
     * searching a single movie by a HTTPS request and getting the JSON
     * @param name : user input movie name
     */
    private fun searchData(name: String) {
        // collecting all the JSON string
        var stb = StringBuilder("")

        //URL with t as the parameter for retrieving a single movie with the passed name
        val urlString = "https://www.omdbapi.com/?t=${name}&apikey=1ef0b74d";
        val url = URL(urlString)
        val con: HttpURLConnection = url.openConnection() as HttpURLConnection

        //to get the returned data after reading JSON
        var data:String = ""

        runBlocking {
            launch {
                // run the code of the coroutine in a new thread
                withContext(Dispatchers.IO) {
                    //buffet reader to read the input
                    var bf = BufferedReader(InputStreamReader(con.inputStream))
                    var line: String? = bf.readLine()
                    while (line != null) {
                        //for every line read assigning to the StringBuilder
                        stb.append(line + "\n")
                        line = bf.readLine()
                    }

                    //calling the function where data is parsed from JSON
                    data = parseJSON(stb)

                    //setting the textView with the movie data
                    //running on the UI thread to avoid crashes
                    runOnUiThread{
                        infoText.text = data
                    }
                }
            }
        }
    }

    /**
     * function which populates the tables with the data
     */
    private suspend fun saveToDatabase(){
        //checking if data is retrieved or not... to avoid crashing
        if (isThere){
            //populating the movie table with movie data added in parseJSON fun
            movieDao.addMovie(newMovie)

            for (names in listOfActors){
                //populating actor table one by one actor
                val actor = Actor(names)
                movieDao.addActor(actor)

                //populating the cross ref table with title of the movie and the actors
                val actorWithMovie = ActorMovieCrossRef(titleMovie, names)
                movieDao.addActorMovie(actorWithMovie)
            }
        }
        runOnUiThread {
            Toast.makeText(this, "Added to Database", Toast.LENGTH_LONG).show()
        }
    }

    private fun parseJSON(stb: StringBuilder): String {
        //creating a JSON object from the string
        val json = JSONObject(stb.toString())
        //extracting the default response which returns true or false if movie is found or not
        isThere = json["Response"] == "True"
        //is movie is found
        if (isThere){
            runOnUiThread {
                infoText.visibility = View.VISIBLE
                notFound.visibility = View.GONE
            }

            //string to hold the read data
            var info:String

            //extracting the data of the movie
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

            //assigning the string with the data
            info = "Title: $title\n, Year: $year\n, Rated: $rated\n, Released: $released\n, Runtime: $runtime, Genre: $genre\n, Director: $director\n, Writer: $writer\n, Actors: $actors\n, Plot: $plot"
            //a new movie object with data so can populate
            newMovie = Movie(title,year, rated,released,runtime,genre,director,writer,actors,plot)
            //assigning the title..for populating actor with movie table
            titleMovie = title

            //all the actors separated by commas as they all come as one string
            listOfActors = listOf(*actors.split(",").toTypedArray())  //https://www.techiedelight.com/convert-string-to-list-kotlin/

            return info
        }else{
            runOnUiThread {
                //code that runs in main
                notFound.visibility = View.VISIBLE
                infoText.visibility = View.GONE
            }
            //returning null
            return ""
        }
    }

    /**
     * save the name user inputted
     * @param outState
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("name", name)

    }

    /**
     * restoring the name and calling the searchData function after orientation
     * @param savedInstanceState
     */
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        name = savedInstanceState.getString("name")!!
        //only user input if not null
        if (name != ""){
            searchData(name)
        }

    }
}