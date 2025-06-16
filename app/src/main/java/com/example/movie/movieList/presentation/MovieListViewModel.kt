package com.example.movie.movieList.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movie.movieList.domain.model.Movie
import com.example.movie.movieList.domain.repository.MovieListRepository
import com.example.movie.movieList.util.Category
import com.example.movie.movieList.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val movieListRepository: MovieListRepository

) : ViewModel() {
    private val _movieListState = MutableStateFlow(MovieListState())
    val movieListState = _movieListState.asStateFlow()



    init {
        getPopularMovieList(false)
        getUpcomingMovieList(false)

    }

    fun onEvent(event: MovieListUiEvent) {
        when(event) {
            MovieListUiEvent.Navigate -> {
                _movieListState.update {
                    it.copy(
                       // isCurrentPopularScreen = !movieListState.value.isCurrentPopularScreen
                    )
                }
            }
            is MovieListUiEvent.Paginate -> {
                if (event.category == Category.POPULAR) {
                    getPopularMovieList(true)
                } else if (event.category == Category.UPCOMING) {
                    getUpcomingMovieList(true)
                }
            }
            is MovieListUiEvent.Search -> {
                getSearchMoviesList(event.query)
            }

            MovieListUiEvent.ClearSearch -> {
                _movieListState.update { it.copy(searchMovieList = emptyList(), searchQuery = "") }
            }
            MovieListUiEvent.SetAiMoviesLoading -> {
                setAiMoviesLoading()
            }
            is MovieListUiEvent.SetAiMovies -> {
                setAiMovies(event.movies)
            }


        }
    }
    private fun getPopularMovieList(forceFetchFromRemote: Boolean ) {
        viewModelScope.launch {
            _movieListState.update {
                it.copy(isLoading = true)
            }
            movieListRepository.getMovieList(
                forceFetchFromRemote,
                Category.POPULAR,
                movieListState.value.popularMovieListPage
            ).collectLatest { result ->
                when (result) {
                    is Resource.Error -> {
                        _movieListState.update {
                            it.copy(isLoading = false)
                        }
                    }

                    is Resource.Success -> {
                        result.data?.let{ popularList ->
                            _movieListState.update {
                                it.copy(
                                    popularMovieList = movieListState.value.popularMovieList
                                    + popularList.shuffled(),
                                    popularMovieListPage = movieListState.value.popularMovieListPage + 1,
                                )
                            }

                        }

                    }

                    is Resource.Loading -> {
                        _movieListState.update {
                            it.copy(isLoading = result.isLoading)
                        }

                    }

                }
            }
        }

    }
    private fun getUpcomingMovieList(forceFetchFromRemote: Boolean ) {
        viewModelScope.launch {
            _movieListState.update {
                it.copy(isLoading = true)
            }
            movieListRepository.getMovieList(
                forceFetchFromRemote,
                Category.UPCOMING,
                movieListState.value.upcomingMovieListPage
            ).collectLatest { result ->
                when (result) {
                    is Resource.Error -> {
                        _movieListState.update {
                            it.copy(isLoading = false)
                        }
                    }

                    is Resource.Success -> {
                        result.data?.let{ upcomingList ->
                            _movieListState.update {
                                it.copy(
                                    upcomingMovieList = movieListState.value.upcomingMovieList
                                            + upcomingList.shuffled(),
                                    upcomingMovieListPage = movieListState.value.upcomingMovieListPage + 1,
                                )
                            }

                        }

                    }

                    is Resource.Loading -> {
                        _movieListState.update {
                            it.copy(isLoading = result.isLoading)
                        }

                    }

                }
            }
        }
    }


    private fun getSearchMoviesList(query: String) {
        clearAiMovies()
        viewModelScope.launch {
            _movieListState.update { it.copy(isLoading = true, searchQuery = query, searchMovieList = emptyList(), searchPage = 1) }
            movieListRepository.searchMovies(query, 1).collectLatest { result ->
                when (result) {
                    is Resource.Error -> _movieListState.update { it.copy(isLoading = false) }
                    is Resource.Success -> result.data?.let { list ->
                        _movieListState.update { it.copy(searchMovieList = list, isLoading = false) }
                    }
                    is Resource.Loading -> _movieListState.update { it.copy(isLoading = result.isLoading) }
                }
            }
        }
    }

    fun setCurrentScreen(screen: String) {
        _movieListState.update {
            it.copy(
                isCurrentPopularScreen = screen == "popular",
                isCurrentUpcomingScreen = screen == "upcoming",
                isCurrentSearchScreen = screen == "search",
                isCurrentWatchedScreen = screen == "watched"
            )
        }
    }

    fun setAiMoviesLoading() {
        _movieListState.update {
            it.copy(isLoading = true, isAiSearch = true)
        }
    }

    fun clearAiMovies() {
        _movieListState.update {
            it.copy(
                aiMovieList = emptyList(),
                isAiSearch = false
            )
        }
    }

    fun setAiMovies(movies: List<Movie>) {
        _movieListState.update {
            it.copy(
                aiMovieList = movies,
                isLoading = false,
                isAiSearch = true
            )
        }
    }



}