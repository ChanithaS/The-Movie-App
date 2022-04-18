package com.example.movieapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "actor_table")
data class Actor(
    //table which contain all the actors .. with primary key as actor name
    @PrimaryKey val actor_name: String,
)
