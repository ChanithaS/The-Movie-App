package com.example.movieapp.data

import androidx.room.*

@Dao
interface MovieDao {
        @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addMovie(vararg movies: Movie)

    @Query("SELECT * FROM movie_table")
    suspend fun readData(): List<Movie>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addActor(vararg actors: Actor)

    @Query("SELECT * FROM actor_table")
    suspend fun getActor(): List<Actor>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addActorMovie(vararg crossRef: ActorMovieCrossRef)

    @Transaction
    @Query("SELECT * FROM actor_table WHERE actor_name LIKE '%' || :key_Word || '%'")
    fun getActorMovies(key_Word:String): List<ActorWithMovies>
}