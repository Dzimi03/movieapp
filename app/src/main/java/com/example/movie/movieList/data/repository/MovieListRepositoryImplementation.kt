package com.example.movie.movieList.data.repository
import coil.network.HttpException
import com.example.movie.movieList.data.local.movie.MovieDatabase
import com.example.movie.movieList.data.mappers.toMovie
import com.example.movie.movieList.data.mappers.toMovieEntity
import com.example.movie.movieList.data.remote.MovieApi
import com.example.movie.movieList.domain.model.Movie
import com.example.movie.movieList.domain.repository.MovieListRepository
import com.example.movie.movieList.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import javax.inject.Inject

class MovieListRepositoryImplementation @Inject constructor(
    private val movieApi: MovieApi,
    private val movieDatabase: MovieDatabase
) : MovieListRepository {
    override suspend fun getMovieList(
        forceFetchFromRemote: Boolean,
        category: String,
        page: Int
    ): Flow<Resource<List<Movie>>> {
        return flow {
            emit(Resource.Loading(true))
            val localMovieList = movieDatabase.movieDao.getMovieListByCategory(category)
            val shouldLoadLocalMovie = localMovieList.isNotEmpty() && !forceFetchFromRemote
            if (shouldLoadLocalMovie) {
                emit(Resource.Success(
                    data = localMovieList.map { movieEntity ->
                        movieEntity.toMovie(category)
                    }
                ))
                emit(Resource.Loading(false))
                return@flow
            }

            val movieListFromApi = try {
                movieApi.getMovieList(category, page)
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error("Couldn't load data"))
                return@flow
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error("Couldn't load data"))
                return@flow
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Resource.Error("Couldn't load data"))
                return@flow
            }

            val movieEntities = movieListFromApi.results.let {
                it.map { movieDto ->
                    movieDto.toMovieEntity(category)
                }
            }

            movieDatabase.movieDao.upsertMovieList(movieEntities)
            emit(Resource.Success(
                movieEntities.map { it.toMovie(category) }

            ))
            emit(Resource.Loading(false))
        }

    }
    override suspend fun getMovie(id: Int): Flow<Resource<Movie>> {
        return flow {
            emit(Resource.Loading(true))
            val movieEntity = movieDatabase.movieDao.getMovieById(id)
            if (movieEntity != null) {
                emit(Resource.Success(movieEntity.toMovie(movieEntity.category)))
                emit(Resource.Loading(false))
                return@flow
            }
            // Pobierz szczegóły z API, jeśli nie ma w bazie
            try {
                val movieDto = movieApi.getMovieDetails(id, MovieApi.API_KEY) // Dodaj taki endpoint w MovieApi!
                val movie = movieDto.toMovie("search")
                emit(Resource.Success(movie))
            } catch (e: Exception) {
                emit(Resource.Error("Couldn't load data"))
            }
            emit(Resource.Loading(false))
        }
    }

    override suspend fun searchMovies(query: String, page: Int): Flow<Resource<List<Movie>>> {
        return flow {
            emit(Resource.Loading(true))
            val movieListFromApi = try {
                movieApi.searchMovies(query, page)
            } catch (e: Exception) {
                emit(Resource.Error("Błąd wyszukiwania"))
                return@flow
            }
            val movies = movieListFromApi.results.map { movieDto ->
                movieDto.toMovie("search")
            }
            emit(Resource.Success(movies))
            emit(Resource.Loading(false))
        }
    }

}