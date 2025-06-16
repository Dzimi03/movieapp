package com.example.movie.details.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ImageNotSupported
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role.Companion.Button
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.movie.movieList.data.remote.MovieApi
import coil.size.Size
import com.example.movie.R
import com.example.movie.movieList.data.local.movie.WatchedMovieEntity
import com.example.movie.movieList.presentation.WatchedViewModel
import com.example.movie.movieList.util.RatingBar
import com.example.movie.movieList.util.getAverageColor

@Composable
fun DetailsScreen() {

    val detailsViewModel = hiltViewModel<DetailsViewModel>()
    val detailsState = detailsViewModel.detailsState.collectAsState().value
    val watchedViewModel = hiltViewModel<WatchedViewModel>()

    LaunchedEffect(Unit) {
        watchedViewModel.loadLists()
    }

    val watched = watchedViewModel.watched.collectAsState().value
    val toWatch = watchedViewModel.toWatch.collectAsState().value

    val movieId = detailsState.movie?.id
    val isWatched = watched.any { it.id == movieId }
    val isToWatch = toWatch.any { it.id == movieId }


    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 80.dp) // zostaw miejsce na przyciski
        ) {




            val backDropImageState = rememberAsyncImagePainter(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(MovieApi.IMAGE_BASE_URL + detailsState.movie?.backdrop_path)
                    .size(Size.ORIGINAL)
                    .build()
            ).state

            val posterImageState = rememberAsyncImagePainter(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(MovieApi.IMAGE_BASE_URL + detailsState.movie?.poster_path)
                    .size(Size.ORIGINAL)
                    .build()
            ).state


                if (backDropImageState is AsyncImagePainter.State.Error) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center

                    ) {
                        Icon(
                            modifier = Modifier.size(70.dp),
                            imageVector = Icons.Rounded.ImageNotSupported,
                            contentDescription = detailsState.movie?.title
                        )
                    }
                }

                if (backDropImageState is AsyncImagePainter.State.Success) {
                    Image(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp),
                        painter = backDropImageState.painter,
                        contentDescription = detailsState.movie?.title,
                        contentScale = ContentScale.Crop

                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {

                    Box(
                        modifier = Modifier
                            .width(160.dp)
                            .height(240.dp)
                    ) {
                        if (posterImageState is AsyncImagePainter.State.Error) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(MaterialTheme.colorScheme.primaryContainer),
                                contentAlignment = Alignment.Center

                            ) {
                                Icon(
                                    modifier = Modifier.size(70.dp),
                                    imageVector = Icons.Rounded.ImageNotSupported,
                                    contentDescription = detailsState.movie?.title
                                )
                            }
                        }

                        if (posterImageState is AsyncImagePainter.State.Success) {
                            Image(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(12.dp)),
                                painter = posterImageState.painter,
                                contentDescription = detailsState.movie?.title,
                                contentScale = ContentScale.Crop

                            )
                        }
                    }

                    detailsState.movie?.let { movie ->
                        Column(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                modifier = Modifier.padding(start = 16.dp),
                                text = movie.title,
                                fontSize = 19.sp,
                                fontWeight = FontWeight.SemiBold
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Row(
                                modifier = Modifier
                                    .padding(start = 16.dp)
                            ) {
                                RatingBar(
                                    starsModifier = Modifier.size(18.dp),
                                    rating = movie.vote_average / 2,

                                    )

                                Text(
                                    modifier = Modifier.padding(start = 4.dp),
                                    text = movie.vote_average.toString().take(3),
                                    color = Color.LightGray,
                                    fontSize = 14.sp,
                                    maxLines = 1

                                )

                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                modifier = Modifier.padding(start = 16.dp),
                                text = stringResource(R.string.language) + movie.original_language,
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                modifier = Modifier.padding(start = 16.dp),
                                text = stringResource(R.string.release_date) + movie.release_date,
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                modifier = Modifier.padding(start = 16.dp),
                                text = movie.vote_count.toString() + stringResource(R.string.votes),
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    modifier = Modifier.padding(start = 16.dp),
                    text = stringResource(R.string.overview),
                    fontSize = 19.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(8.dp))

                detailsState.movie?.let {
                    Text(
                        modifier = Modifier.padding(start = 16.dp),
                        text = it.overview,
                        fontSize = 16.sp,
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))



        }











        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = {
                    detailsState.movie?.let { movie ->
                        val entity = WatchedMovieEntity(
                            id = movie.id,
                            title = movie.title,
                            poster_path = movie.poster_path,
                            isWatched = false
                        )
                        if (isToWatch) {
                            watchedViewModel.removeMovie(entity)
                        } else {
                            watchedViewModel.addMovie(entity)
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isToWatch) Color(0xFF1976D2) else MaterialTheme.colorScheme.primary,
                    contentColor = if (isToWatch) Color.White else MaterialTheme.colorScheme.onPrimary
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = if (isToWatch) "Usuń z 'Do obejrzenia'" else "Do obejrzenia",
                    fontWeight = if (isToWatch) FontWeight.Bold else FontWeight.Normal
                )
            }
            Button(
                onClick = {
                    detailsState.movie?.let { movie ->
                        val entity = WatchedMovieEntity(
                            id = movie.id,
                            title = movie.title,
                            poster_path = movie.poster_path,
                            isWatched = true
                        )
                        if (isWatched) {
                            watchedViewModel.removeMovie(entity)
                        } else {
                            watchedViewModel.addMovie(entity)
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isWatched) Color(0xFF388E3C) else MaterialTheme.colorScheme.primary,
                    contentColor = if (isWatched) Color.White else MaterialTheme.colorScheme.onPrimary
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = if (isWatched) "Usuń z 'Obejrzane'" else "Obejrzane",
                    fontWeight = if (isWatched) FontWeight.Bold else FontWeight.Normal
                )
            }
        }



    }

}