/***********************************************************************************************************************
 * Name     : R.K Chanitha Sankalpana                                                                                  *
 * IIT ID   : 20200618                                                                                                 *
 * UoW ID   : w1833590                                                                                                 *
 * http://www.omdbapi.com/  Web service was used with a free account
 **********************************************************************************************************************/

package com.example.movieapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.room.Room
import com.example.movieapp.data.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {
    private lateinit var movieDao: MovieDao      //to get access to the database

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //hiding the action bar
        supportActionBar?.hide()

        //initializing the database and the movie dao
        val db = Room.databaseBuilder(this, MovieDatabase::class.java, "mydatabase").build()
        movieDao = db.movieDao()

        //initializing elements
        val addToDB = findViewById<Button>(R.id.addMovieBtn)
        val searchMovie = findViewById<Button>(R.id.searchMovieBtn)
        val searchActorData = findViewById<Button>(R.id.searchActorsBtn)
        val searchMovieOnline = findViewById<Button>(R.id.searchOnlineBtn)

        //button for adding data to DB
        addToDB.setOnClickListener {
            addData()
        }
        //calling new activities
        searchMovie.setOnClickListener {
            val searchMovies = Intent(this, SearchMovieActivity::class.java)
            startActivity(searchMovies)
        }
        searchActorData.setOnClickListener {
            val searchActor = Intent(this, SearchActorActivity::class.java)
            startActivity(searchActor)
        }
        searchMovieOnline.setOnClickListener {
            val searchOnline = Intent(this, SearchOnlineActivity::class.java)
            startActivity(searchOnline)
        }
    }

    /**
     * function responsible for adding data to DB
     */
    private fun addData() {
        //checking if the movies are added to the DB
        var count = 0
        //to reading the database coroutines is a must
        runBlocking {
            launch {
                //assigning a list the passed movie list
                val movies: List<Movie> = movieDao.readData()
                for (u in movies) {
                    if (u.Title == "Tim Robbins, Morgan Freeman, Bob Gunton" || u.Title == "Batman: The Dark Knight Returns, Part 1"
                        || u.Title == "The Lord of the Rings: The Return of the King" || u.Title == "Inception"
                        || u.Title == "The Matrix"){
                            //detect if all 5 movies are there by increating the count
                            count++
                    }
                }
            }
        }

        if (count >= 4){
            Toast.makeText(this, "Already added to Database", Toast.LENGTH_LONG).show()
        }else{
            //default movies.. actors are separated to add them to actors tables
            val movie1Actors = "Tim Robbins, Morgan Freeman, Bob Gunton"
            val movie1 = Movie(
                "The Shawshank Redemption",
                "1994",
                "R",
                "14 Oct 1994",
                "142 min",
                "Drama",
                "Frank Darabont",
                "Stephen King, Frank Darabont", movie1Actors,
                "Two imprisoned men bond over a number of years, finding solace and eventual redemption through acts of common decency"
            )
            val movie2Actors = "Peter Weller, Ariel Winter, David Selby, Wade Williams"
            val movie2 = Movie(
                "Batman: The Dark Knight Returns, Part 1",
                "2012",
                "PG-13",
                "25 Sep 2012",
                "76 min",
                "Animation, Action, Crime, Drama, Thriller",
                "Jay Oliva",
                "Bob Kane (character created by: Batman), Frank Miller (comic book), Klaus Janson (comic book), Bob Goodman",
                movie2Actors,
                "Batman has not been seen for ten years. A new breed" +
                        "of criminal ravages Gotham City, forcing 55-year-old Bruce Wayne back" +
                        "into the cape and cowl. But, does he still have what it takes to fight" +
                        "crime in a new era?"
            )
            val movie3Actors = "Elijah Wood, Viggo Mortensen, Ian McKellen"
            val movie3 = Movie(
                "The Lord of the Rings: The Return of the King",
                "2003",
                "PG-13",
                "17 Dec 2003",
                "201 min",
                "Action, Adventure, Drama",
                "Peter Jackson",
                "J.R.R. Tolkien, Fran Walsh, Philippa Boyens",
                movie3Actors,
                "Gandalf and Aragorn lead the World of Men against Sauron's army to draw his gaze from Frodo and Sam as they approach Mount Doom with the One Ring."
            )
            val movie4Actors = "Leonardo DiCaprio, Joseph Gordon-Levitt, Elliot Page"
            val movie4 = Movie(
                "Inception",
                "2010",
                "PG-13",
                "16 Jul 2010",
                "148 min",
                "Action, Adventure, Sci-Fi",
                "Christopher Nolan",
                "Christopher Nolan",
                movie4Actors,
                "A thief who steals corporate secrets through the use of dream-sharing technology " +
                        "is given the inverse task of planting an idea into the mind of a C.E.O., but his tragic past may doom the project and his team to disaster."
            )
            val movie5Actors = "Keanu Reeves, Laurence Fishburne, Carrie-Anne Moss"
            val movie5 = Movie(
                "The Matrix",
                "1999",
                "R",
                "31 Mar 1999",
                "136 min",
                "Action, Sci-Fi",
                "Lana Wachowski, Lilly Wachowski",
                "Lilly Wachowski, Lana Wachowski",
                movie5Actors,
                "When a beautiful stranger leads computer hacker Neo " +
                        "to a forbidding underworld, he discovers the shocking truth--the life he knows is the elaborate deception of an evil cyber-intelligence."
            )

            runBlocking {
                launch {
                    //populating the movies tables
                    movieDao.addMovie(movie1, movie2, movie3, movie4, movie5)

                    //all movie titles
                    val moviesArr = arrayOf(
                        "The Shawshank Redemption", "Batman: The Dark Knight Returns, Part 1",
                        "The Lord of the Rings: The Return of the King", "Inception", "The Matrix"
                    )

                    //creating a 2d array of all the actors
                    val actorsArr = arrayOf(movie1Actors, movie2Actors, movie3Actors, movie4Actors, movie5Actors)
                    for ((index, value) in actorsArr.withIndex()) {
                        //separating the index actors arrays from commas to get the names separately
                        val list: List<String> = listOf(*value.split(",").toTypedArray())
                        for (names in list) {
                            //populating the actors table with all the actors
                            val actor = Actor(names)
                            movieDao.addActor(actor)

                            //populating the actors and movies table with the index movie name and actor name
                            val actorWithMovie = ActorMovieCrossRef(moviesArr[index], names)
                            movieDao.addActorMovie(actorWithMovie)
                        }
                    }
                }
            }
            Toast.makeText(this, "5 Movies added to Database", Toast.LENGTH_LONG).show()
        }
    }
}