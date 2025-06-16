package com.example.movie.movieList.data.local.movie

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Delete

@Dao
interface MovieDao {
    @Upsert
    suspend fun upsertMovieList(movieList: List<MovieEntity>)

    @Query("SELECT * FROM MovieEntity WHERE id = :id")
    suspend fun getMovieById(id: Int): MovieEntity

    @Query("SELECT * FROM MovieEntity WHERE category = :category")
    suspend fun getMovieListByCategory(category: String): List<MovieEntity>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertWatchedMovie(movie: WatchedMovieEntity)

    @Delete
    suspend fun deleteWatchedMovie(movie: WatchedMovieEntity)

    @Query("SELECT * FROM WatchedMovieEntity WHERE isWatched = 1")
    suspend fun getWatchedMovies(): List<WatchedMovieEntity>

    @Query("SELECT * FROM WatchedMovieEntity WHERE isWatched = 0")
    suspend fun getToWatchMovies(): List<WatchedMovieEntity>
}