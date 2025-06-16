package com.example.movie.movieList.domain.repository

import com.example.movie.movieList.util.Resource
import kotlinx.coroutines.flow.Flow
import com.example.movie.movieList.domain.model.Movie

interface MovieListRepository {
    suspend fun getMovieList(
        forceFetchFromRemote: Boolean,
        category: String,
        page: Int

    ) : Flow<Resource<List<Movie>>>

    suspend fun getMovie(id: Int): Flow<Resource<Movie>>

    suspend fun searchMovies(query: String, page: Int): Flow<Resource<List<Movie>>>

}