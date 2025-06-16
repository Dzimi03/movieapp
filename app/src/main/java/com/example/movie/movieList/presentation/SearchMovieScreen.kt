package com.example.movie.movieList.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.movie.movieList.data.remote.getMoviesFromGemini
import com.example.movie.movieList.domain.model.Movie
import com.example.movie.movieList.presentation.components.MovieItem



@Composable
fun SearchMoviesScreen(
    movieListState: MovieListState,
    navController: NavHostController,
    onEvent: (MovieListUiEvent) -> Unit
) {
    var query by remember { mutableStateOf(movieListState.searchQuery) }
    var aiQuery by remember { mutableStateOf("") }
    val aiMovies = movieListState.aiMovieList
    val isAiLoading = movieListState.isLoading && movieListState.isAiSearch


    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            label = { Text("Wyszukaj film") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                if (query.length > 2) {
                    onEvent(MovieListUiEvent.Search(query))
                }
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Szukaj")
        }

        Spacer(modifier = Modifier.height(16.dp))


        OutlinedTextField(
            value = aiQuery,
            onValueChange = { aiQuery = it },
            label = { Text("Describe what you want to watch (AI)") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
        Button(
            onClick = {
                onEvent(MovieListUiEvent.ClearSearch) // wyczyść zwykłe wyniki
                onEvent(MovieListUiEvent.SetAiMoviesLoading) // dodaj taki event jeśli chcesz loading
                getMoviesFromGemini(aiQuery) { movies ->
                    onEvent(MovieListUiEvent.SetAiMovies(movies)) // dodaj taki event
                }
            },
            modifier = Modifier.padding(8.dp)
        ) {
            Text("Search AI")
        }

        if (isAiLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        }

        // Wyświetl filmy z AI jeśli są
        if (aiMovies.isNotEmpty()) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 8.dp, horizontal = 4.dp)
            ) {
                items(aiMovies.size) { index ->
                    MovieItem(
                        movie = aiMovies[index],
                        navHostController = navController
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
            return@Column // nie pokazuj klasycznej listy jeśli są filmy z AI
        }


        if (movieListState.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else if (movieListState.searchMovieList.isEmpty() && query.isNotBlank()) {
            Text("Brak wyników", modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 8.dp, horizontal = 4.dp)
            ) {
                items(movieListState.searchMovieList.size) { index ->
                    MovieItem(
                        movie = movieListState.searchMovieList[index],
                        navHostController = navController
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}