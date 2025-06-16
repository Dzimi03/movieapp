package com.example.movie.movieList.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movie.movieList.data.local.movie.WatchedMovieEntity
import com.example.movie.movieList.domain.repository.WatchedRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WatchedViewModel @Inject constructor(
    private val repository: WatchedRepository
): ViewModel() {
    private val _watched = MutableStateFlow<List<WatchedMovieEntity>>(emptyList())
    val watched: StateFlow<List<WatchedMovieEntity>> = _watched

    private val _toWatch = MutableStateFlow<List<WatchedMovieEntity>>(emptyList())
    val toWatch: StateFlow<List<WatchedMovieEntity>> = _toWatch

    fun loadLists() {
        viewModelScope.launch {
            _watched.value = repository.getWatchedMovies()
            _toWatch.value = repository.getToWatchMovies()
        }
    }

    fun addMovie(movie: WatchedMovieEntity) {
        viewModelScope.launch {
            repository.addMovie(movie)
            loadLists()
        }
    }

    fun removeMovie(movie: WatchedMovieEntity) {
        viewModelScope.launch {
            repository.removeMovie(movie)
            loadLists()
        }
    }
}