import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.movie.movieList.data.local.movie.WatchedMovieEntity
import com.example.movie.movieList.presentation.components.MovieItem
import com.example.movie.movieList.domain.model.Movie
import com.example.movie.movieList.presentation.WatchedViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WatchedScreen(
    viewModel: WatchedViewModel = hiltViewModel(),
    navController: NavHostController
) {
    val watched = viewModel.watched.collectAsState().value
    val toWatch = viewModel.toWatch.collectAsState().value
    var selectedTab by remember { mutableStateOf(0) } // 0 - Do obejrzenia, 1 - Obejrzane

    LaunchedEffect(Unit) { viewModel.loadLists() }

    Column(Modifier.fillMaxSize().padding(8.dp)) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = { selectedTab = 0 },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedTab == 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondaryContainer
                ),
                modifier = Modifier.weight(1f)
            ) {
                Text("Do obejrzenia")
            }
            Spacer(Modifier.width(8.dp))
            Button(
                onClick = { selectedTab = 1 },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedTab == 1) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondaryContainer
                ),
                modifier = Modifier.weight(1f)
            ) {
                Text("Obejrzane")
            }
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize()
        ) {
            val movies = if (selectedTab == 0) toWatch else watched
            items(movies) { movie ->
                MovieItem(
                    movie = movie.toMovie(),
                    navHostController = navController
                )
            }
        }
    }
}

// Mapper bez zmian
fun WatchedMovieEntity.toMovie(): Movie = Movie(
    id = id,
    title = title,
    poster_path = poster_path,
    backdrop_path = poster_path,
    genre_ids = emptyList(),
    original_language = "",
    original_title = "",
    overview = "",
    popularity = 0.0,
    release_date = "",
    video = false,
    vote_average = vote_average,
    vote_count = vote_count,
    adult = false,
    category = ""
)