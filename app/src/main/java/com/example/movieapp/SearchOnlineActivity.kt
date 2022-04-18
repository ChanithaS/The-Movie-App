package com.example.movieapp

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.movieapp.adapters.OnlineMoviesAdapter
import com.example.movieapp.data.MovieDao
import com.example.movieapp.data.MovieDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class SearchOnlineActivity : AppCompatActivity() {
    private lateinit var movieDao: MovieDao
    private lateinit var nameEntered: SearchView        //user input
    lateinit var recyclerView: RecyclerView             //displaying data retrieved
    lateinit var notFound : ImageView
    private var foundBool: Boolean = false              //check if data retrieved or not
    var name: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_online)
        supportActionBar?.hide()

        //initializing the variables
        val db = Room.databaseBuilder(this, MovieDatabase::class.java, "mydatabase").build()
        movieDao = db.movieDao()

        val searchOnline = findViewById<Button>(R.id.searchBtnOnline)
        recyclerView = findViewById(R.id.recycleViewOnline)
        nameEntered = findViewById(R.id.movieSearchOnline)
        notFound = findViewById(R.id.movieNotFound)
        notFound.visibility = View.GONE
        recyclerView.visibility = View.GONE

        //for starting search after user input
        searchOnline.setOnClickListener {
            name = nameEntered.query.toString()
            searchOnlineData(name)
        }
    }

    /**
     * searching all the movies with input word and displaying using recycle view
     * @param name = user input name
     */
    private fun searchOnlineData(name: String) {

        //URL with s which returnes multiple results... added with two * to create a query LIKE url
        val urlString = "https://www.omdbapi.com/?s=*${name}*&apikey=1ef0b74d";
        val url = URL(urlString)
        val con: HttpURLConnection = url.openConnection() as HttpURLConnection

        // collecting all the JSON string
        var stringBuilder = StringBuilder("")

        runBlocking {
            launch {
                // run the code of the coroutine in a new thread
                withContext(Dispatchers.IO) {

                    //reading the file from the HTTPS request
                    val buffetReader = BufferedReader(InputStreamReader(con.inputStream))
                    var line: String? = buffetReader.readLine()
                    while (line != null) {
                        //appending to string builder
                        stringBuilder.append(line)
                        line = buffetReader.readLine()
                    }
                }
            }
        }

        //JSONObject with the string
        val json = JSONObject(stringBuilder.toString())
        //extracting the default response which returns true or false if movie is found or not
        foundBool = json["Response"] == "True"

        //if found displaying the data
        if (foundBool) {
            recyclerView.visibility = View.VISIBLE
            notFound.visibility = View.GONE

            //getting the Object array inside the main object which contains the data of movies
            var jsonArray: JSONArray = json.getJSONArray("Search")

            //passing the array of data as JSON to the recycleView... which the reads them inside and displays
            val adapter = OnlineMoviesAdapter(jsonArray)
            //setting layout as linearLayout
            val linearLayoutManager = LinearLayoutManager(this)
            recyclerView.layoutManager = linearLayoutManager
            recyclerView.adapter = adapter

        }else{
            //if not found displaying not found image
            runOnUiThread {
                //code that runs in main
                notFound.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
            }
        }
    }

    /**
     * saving user input name
     * @param outState
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("name", name)

    }

    /**
     * restores after orientation
     * @param savedInstanceState
     */
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        name = savedInstanceState.getString("name")!!
        //if not null only calls the function to avoid crashing
        if (name != ""){
            searchOnlineData(name)
        }
    }

}