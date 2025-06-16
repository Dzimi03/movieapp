package com.example.movie.movieList.data.local.movie

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class WatchedMovieEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val poster_path: String,
    val isWatched: Boolean // true = obejrzane, false = do obejrzenia
)
