package com.example.movie.movieList.data.remote
import com.example.movie.movieList.data.remote.respond.MovieDto
import com.example.movie.movieList.data.remote.respond.MovieListDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


val API_KEY = "0605771259de8763a0e230590920abde"

interface MovieApi {
    @GET("movie/{category}")
    suspend fun getMovieList(
        @Path("category") category: String,
        @Query("page") page: Int,
        @Query("api_key") apiKey: String = API_KEY
    ) : MovieListDto

    @GET("search/movie")
    suspend fun searchMovies(
        @Query("query") query: String,
        @Query("page") page: Int = 1,
        @Query("api_key") apiKey: String = API_KEY
    ): MovieListDto


    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String = API_KEY
    ): MovieDto

    companion object {
        val BASE_URL = "https://api.themoviedb.org/3/"
        val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500"
        val API_KEY = "0605771259de8763a0e230590920abde"
    }

}