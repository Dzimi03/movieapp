package com.example.movie.movieList.presentation

import com.example.movie.movieList.domain.model.Movie

sealed interface MovieListUiEvent{
    data class Paginate(val category: String): MovieListUiEvent
    object Navigate : MovieListUiEvent
    data class Search(val query: String): MovieListUiEvent
    object ClearSearch : MovieListUiEvent
    object SetAiMoviesLoading : MovieListUiEvent
    data class SetAiMovies(val movies: List<Movie>) : MovieListUiEvent

}