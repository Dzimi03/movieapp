package com.example.movie.movieList.data.repository

import com.example.movie.movieList.data.local.movie.MovieDao
import com.example.movie.movieList.data.local.movie.WatchedMovieEntity
import com.example.movie.movieList.domain.repository.WatchedRepository
import javax.inject.Inject

class WatchedRepositoryImpl @Inject constructor(
    private val dao: MovieDao
): WatchedRepository {
    override suspend fun addMovie(movie: WatchedMovieEntity) = dao.upsertWatchedMovie(movie)
    override suspend fun removeMovie(movie: WatchedMovieEntity) = dao.deleteWatchedMovie(movie)
    override suspend fun getWatchedMovies() = dao.getWatchedMovies()
    override suspend fun getToWatchMovies() = dao.getToWatchMovies()
}