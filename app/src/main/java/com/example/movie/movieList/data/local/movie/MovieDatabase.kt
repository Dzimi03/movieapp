package com.example.movie.movieList.data.local.movie
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [MovieEntity::class, WatchedMovieEntity::class],
    version = 2
)
abstract class MovieDatabase: RoomDatabase()  {
    abstract val movieDao: MovieDao

}