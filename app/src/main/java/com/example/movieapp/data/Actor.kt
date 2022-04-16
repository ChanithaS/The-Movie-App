package com.example.movieapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "actor_table")
data class Actor(
    @PrimaryKey val actor_name: String,
)
