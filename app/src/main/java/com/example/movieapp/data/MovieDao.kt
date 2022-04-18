package com.example.movieapp.data

import androidx.room.*

@Dao
interface MovieDao {
    //ignores if the same info comes

    /**
     * inserting movie info to the movie table as vararg for multiple inserting
     * @param movies = movie object
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addMovie(vararg movies: Movie)

    /**
     *reads all the data from movie table
     * @return returns movie data as a list
     */
    @Query("SELECT * FROM movie_table")
    suspend fun readData(): List<Movie>

    /**
     * insert the actors to actor table
     * @param actors = actors
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addActor(vararg actors: Actor)

    /**
     * @return retrieve the actors as a list
     */
    @Query("SELECT * FROM actor_table")
    suspend fun getActor(): List<Actor>

    /**
     * @param crossRef inserting the actor with the movie for the relationship
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addActorMovie(vararg crossRef: ActorMovieCrossRef)

    /**
     * query for retrieving all the actors having similar letters as passed keyword and the movies attended by them
     * @param key_Word = user input actor name
     * @return all actors and movies
     */
    @Transaction
    @Query("SELECT * FROM actor_table WHERE actor_name LIKE '%' || :key_Word || '%'")
    fun getActorMovies(key_Word:String): List<ActorWithMovies>
}