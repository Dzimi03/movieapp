package com.example.movie.di

import com.example.movie.movieList.data.repository.MovieListRepositoryImplementation
import com.example.movie.movieList.data.repository.WatchedRepositoryImpl
import com.example.movie.movieList.domain.repository.MovieListRepository
import com.example.movie.movieList.domain.repository.WatchedRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindMovieListRepository(
        movieListRepositoryImplementation: MovieListRepositoryImplementation
    ): MovieListRepository

    @Binds
    @Singleton
    abstract fun bindWatchedRepository(
        impl: WatchedRepositoryImpl
    ): WatchedRepository
}