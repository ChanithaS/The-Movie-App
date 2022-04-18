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
    private lateinit var nameEntered: SearchView
    lateinit var recyclerView: RecyclerView
    lateinit var notFound : ImageView
    private var foundBool: Boolean = false
    var name: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_online)
        supportActionBar?.hide()

        val db = Room.databaseBuilder(this, MovieDatabase::class.java, "mydatabase").build()
        movieDao = db.movieDao()

        val searchOnline = findViewById<Button>(R.id.searchBtnOnline)
        recyclerView = findViewById(R.id.recycleViewOnline)
        nameEntered = findViewById(R.id.movieSearchOnline)
        notFound = findViewById(R.id.movieNotFound)
        notFound.visibility = View.GONE
        recyclerView.visibility = View.GONE

        searchOnline.setOnClickListener {
            name = nameEntered.query.toString()
            searchOnlineData(name)
        }
    }

    private fun searchOnlineData(name: String) {

        // collecting all the JSON string

        val urlString = "https://www.omdbapi.com/?s=*${name}*&apikey=1ef0b74d";
        val url = URL(urlString)
        val con: HttpURLConnection = url.openConnection() as HttpURLConnection

        var stringBuilder = StringBuilder("")
        runBlocking {
            launch {
                // run the code of the coroutine in a new thread
                withContext(Dispatchers.IO) {

                    val buffetReader = BufferedReader(InputStreamReader(con.inputStream))
                    var line: String? = buffetReader.readLine()
                    while (line != null) {

                        stringBuilder.append(line)
                        line = buffetReader.readLine()
                    }
                }
            }
        }

        val json = JSONObject(stringBuilder.toString())
        foundBool = json["Response"] == "True"
        if (foundBool) {
            recyclerView.visibility = View.VISIBLE
            notFound.visibility = View.GONE

            var jsonArray: JSONArray = json.getJSONArray("Search")

            val adapter = OnlineMoviesAdapter(jsonArray)
            val linearLayoutManager = LinearLayoutManager(this)
            recyclerView.layoutManager = linearLayoutManager
            recyclerView.adapter = adapter

        }else{
            runOnUiThread {
                notFound.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
                //code that runs in main
            }
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
            searchOnlineData(name)
        }
    }

}