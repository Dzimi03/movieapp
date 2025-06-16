package com.example.movie.movieList.presentation

import com.example.movie.movieList.domain.model.Movie

data class MovieListState(
    val isLoading: Boolean = false,

    val popularMovieListPage: Int =1,
    val upcomingMovieListPage: Int =1,

    val isCurrentPopularScreen: Boolean = true,
    val isCurrentUpcomingScreen: Boolean = false,
    val isCurrentSearchScreen: Boolean = false,
    val isCurrentWatchedScreen: Boolean = false,

    val popularMovieList: List<Movie> = emptyList(),
    val upcomingMovieList: List<Movie> = emptyList(),
    val searchMovieList: List<Movie> = emptyList(), val searchQuery: String = "", val searchPage: Int = 1,

    val aiMovieList: List<Movie> = emptyList(),
    val isAiSearch: Boolean = false,
)