package com.daryl.mob23quizapp.core.di

import com.daryl.mob23quizapp.core.services.AuthService
import com.daryl.mob23quizapp.data.repositories.UserRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepoModule {
    @Provides
    @Singleton
    fun provideUserRepo(authService: AuthService): UserRepo = UserRepo(authService)
}