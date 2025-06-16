package com.example.movie.movieList.domain.repository

import com.example.movie.movieList.data.local.movie.WatchedMovieEntity

interface WatchedRepository {
    suspend fun addMovie(movie: WatchedMovieEntity)
    suspend fun removeMovie(movie: WatchedMovieEntity)
    suspend fun getWatchedMovies(): List<WatchedMovieEntity>
    suspend fun getToWatchMovies(): List<WatchedMovieEntity>
}