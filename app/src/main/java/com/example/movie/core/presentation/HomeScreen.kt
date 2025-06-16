package com.example.movie.core.presentation

import WatchedScreen
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.List
import androidx.compose.material.icons.rounded.Movie
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Upcoming
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.movie.movieList.presentation.MovieListViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.movie.R
import com.example.movie.movieList.presentation.MovieListUiEvent
import com.example.movie.movieList.presentation.PopularMoviesScreen
import com.example.movie.movieList.presentation.SearchMoviesScreen
import com.example.movie.movieList.presentation.UpcomingMoviesScreen
import com.example.movie.movieList.util.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController) {
    val movieListViewModel = hiltViewModel<MovieListViewModel>()

    val movieListState = movieListViewModel.movieListState.collectAsState().value
    val bottomNavController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                bottomNavController = bottomNavController,
                onEvent = movieListViewModel::onEvent,
                movieListViewModel = movieListViewModel
            )
        },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = when {
                            movieListState.isCurrentPopularScreen -> stringResource(R.string.popular_movies)
                            movieListState.isCurrentUpcomingScreen -> stringResource(R.string.upcoming_movies)
                            movieListState.isCurrentSearchScreen -> stringResource(R.string.search_movies)
                            movieListState.isCurrentWatchedScreen -> stringResource(R.string.my_films)
                            else -> ""
                        },
                        fontSize = 20.sp
                    )
                },
               // modifier = Modifier.padding(it),
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    MaterialTheme.colorScheme.inverseOnSurface
                )
            )


        }) {
        Box(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            NavHost(navController = bottomNavController,
                startDestination = Screen.PopularMovieList.rout
            ) {
                composable(Screen.PopularMovieList.rout) {
                    PopularMoviesScreen(
                        navController = navController,
                        movieListState = movieListState,
                        onEvent = movieListViewModel::onEvent
                    )
                }
                composable(Screen.UpcomingMovieList.rout) {
                    UpcomingMoviesScreen(
                        navController = navController,
                        movieListState = movieListState,
                        onEvent = movieListViewModel::onEvent
                    )
                }

                composable(Screen.SearchMovieList.rout) {
                    SearchMoviesScreen(
                        navController = navController,
                        movieListState = movieListState,
                        onEvent = movieListViewModel::onEvent
                    )
                }

                composable(Screen.WatchedList.rout) {
                    WatchedScreen(
                        viewModel = hiltViewModel(),
                        navController = navController
                    )
                }
            }

        }
    }
}


    @Composable
    fun BottomNavigationBar(
        bottomNavController: NavHostController,
        onEvent: (MovieListUiEvent) -> Unit,
        movieListViewModel: MovieListViewModel,


    ) {
        val items = listOf(
            BottomItem(
                title = stringResource(R.string.popular),
                icon = Icons.Rounded.Movie
            ),
            BottomItem(
                title = stringResource(R.string.upcoming),
                icon = Icons.Rounded.Upcoming
            ),

            BottomItem(
                title = "Search",
                icon = Icons.Rounded.Search
            ),

            BottomItem(
                title = "My Films",
                icon = Icons.Rounded.List
            )

        )

        val selected = rememberSaveable {
            mutableIntStateOf(0)
        }

        NavigationBar {
            Row(
                modifier = Modifier.background(MaterialTheme.colorScheme.inverseOnSurface)
            ) {
                items.forEachIndexed { index, bottomItem ->
                    NavigationBarItem(
                        selected = selected.value == index,
                        onClick = {
                            selected.intValue = index
                            when (selected.intValue) {
                                0 -> {
                                    onEvent(MovieListUiEvent.Navigate)
                                    movieListViewModel.setCurrentScreen("popular")
                                    bottomNavController.popBackStack()
                                    bottomNavController.navigate(Screen.PopularMovieList.rout)
                                }

                                1 -> {
                                    onEvent(MovieListUiEvent.Navigate)
                                    movieListViewModel.setCurrentScreen("upcoming")
                                    bottomNavController.popBackStack()
                                    bottomNavController.navigate(Screen.UpcomingMovieList.rout)
                                }


                                2 -> {
                                    onEvent(MovieListUiEvent.Navigate)
                                    movieListViewModel.setCurrentScreen("search")
                                    bottomNavController.popBackStack()
                                    bottomNavController.navigate(Screen.SearchMovieList.rout)
                                }

                                3 -> {
                                    movieListViewModel.setCurrentScreen("watched")
                                    bottomNavController.popBackStack()
                                    bottomNavController.navigate(Screen.WatchedList.rout)
                                }


                            }
                        },
                        icon = {
                            Icon(
                                imageVector = bottomItem.icon,
                                contentDescription = bottomItem.title,
                                tint = MaterialTheme.colorScheme.onBackground
                                )
                        },
                        label = {
                            Text(text = bottomItem.title,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    )
                }

            }

        }
    }


data class BottomItem(
    val title : String,
    val icon: ImageVector
)




