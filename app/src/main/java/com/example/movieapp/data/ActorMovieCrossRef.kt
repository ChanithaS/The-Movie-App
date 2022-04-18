package com.example.movieapp.data

import androidx.room.Entity

@Entity(primaryKeys = ["Title", "actor_name"])
data class ActorMovieCrossRef(
    //movie name and the actor is stored in this table.. which has many to many relationship
    val Title: String,
    val actor_name: String
)