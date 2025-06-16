package com.example.movie.movieList.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.movie.movieList.presentation.components.MovieItem
import com.example.movie.movieList.util.Category
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp


data class Genre(val id: Int, val name: String)
val genres = listOf(
    Genre(28, "Action"),
    Genre(35, "Comedy"),
    Genre(18, "Drama"),
    Genre(27, "Horror"),
    Genre(10749, "Romance"),
    Genre(16, "Animation"),
    Genre(80, "Crime"),
    Genre(53, "Thriller"),
    Genre(12, "Adventure"),
    Genre(14, "Fantasy"),
    Genre(99, "Documentary"),
    Genre(878, "Sci-Fi"),
    Genre(36, "History"),
    Genre(10402, "Music"),
    Genre(9648, "Mystery"),
    Genre(10751, "Family"),
    // Dodaj więcej jeśli chcesz
)



@Composable
fun PopularMoviesScreen(
    movieListState: MovieListState,
    navController: NavHostController,
    onEvent: (MovieListUiEvent) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedGenre by remember { mutableStateOf<Genre?>(null) }

    Column(modifier = Modifier.fillMaxSize()) {
        // Przycisk otwierający menu gatunków
        Box(modifier = Modifier.padding(8.dp)) {
            Button(onClick = { expanded = true }) {
                Text(selectedGenre?.name ?: "Filter by genre")
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text("All genres") },
                    onClick = {
                        selectedGenre = null
                        expanded = false
                    }
                )
                genres.forEach { genre ->
                    DropdownMenuItem(
                        text = { Text(genre.name) },
                        onClick = {
                            selectedGenre = genre
                            expanded = false
                        }
                    )
                }
            }
        }

        // Filtrowanie filmów po wybranym gatunku
        val filteredMovies = if (selectedGenre != null) {
            movieListState.popularMovieList.filter { it.genre_ids.contains(selectedGenre!!.id) }
        } else {
            movieListState.popularMovieList
        }

        if (filteredMovies.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 8.dp, horizontal = 4.dp)
            ) {
                items(filteredMovies.size) { index ->
                    MovieItem(
                        movie = filteredMovies[index],
                        navHostController = navController
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    if (index >= filteredMovies.size - 1 && !movieListState.isLoading) {
                        onEvent(MovieListUiEvent.Paginate(com.example.movie.movieList.util.Category.POPULAR))
                    }
                }
            }
        }
    }
}