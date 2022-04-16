package com.example.movieapp.data

import androidx.room.Entity

@Entity(primaryKeys = ["Title", "actor_name"])
data class ActorMovieCrossRef(
    val Title: String,
    val actor_name: String
)