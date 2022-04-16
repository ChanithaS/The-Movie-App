package com.example.movieapp.data

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

class ActorWithMovies (
    @Embedded val actor: Actor,
    @Relation(
        parentColumn = "actor_name",
        entityColumn = "Title",
        associateBy = Junction(ActorMovieCrossRef::class)
    )
    val movieAll: List<Movie>
)