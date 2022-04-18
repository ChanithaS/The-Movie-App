package com.example.movieapp.data

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

class ActorWithMovies (
    //for many to many relationship between actor and movie
    @Embedded val actor: Actor,
    @Relation(
        parentColumn = "actor_name",
        entityColumn = "Title",
        associateBy = Junction(ActorMovieCrossRef::class)
    )
    //list containing all the movies the actor has participated
    val movieAll: List<Movie>
)