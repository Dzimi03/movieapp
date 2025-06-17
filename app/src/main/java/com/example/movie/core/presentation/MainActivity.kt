 package com.example.movie.core.presentation

//import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.movie.details.presentation.DetailsScreen
import com.example.movie.movieList.presentation.MovieListViewModel
import com.example.movie.movieList.util.Screen
import com.example.movie.ui.theme.MovieTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint

 @AndroidEntryPoint  //wstrzykiwania zależności przez Hilt.
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MovieTheme {
                SetBarColor(color = MaterialTheme.colorScheme.inverseOnSurface)
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Surface(
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        val navController = rememberNavController()
                        NavHost(
                            navController = navController,
                            startDestination = Screen.Home.rout
                        ) {
                            composable(Screen.Home.rout) {
                                HomeScreen(navController)
                            }
                            composable(Screen.Details.rout + "/{movieId}",
                                    arguments = listOf(
                                        navArgument("movieId") { type = NavType.IntType }
                                    )
                                ) {
                                DetailsScreen()
                            }
                        }

                    }
                }
            }
        }
    }

     @Composable
     private fun SetBarColor (color: Color) {
        val systemUiController = rememberSystemUiController()
         LaunchedEffect(key1 = color) {
             systemUiController.setSystemBarsColor(color)
         }
     }
}

