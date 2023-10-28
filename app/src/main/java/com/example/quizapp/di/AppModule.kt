package com.example.quizapp.di

import com.example.quizapp.data.datasource.remote.ApiService
import com.example.quizapp.data.repository.QuestionsRepositoryImpl
import com.example.quizapp.domain.repository.QuestionsRepository
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private val loggingInterceptor = run {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.apply {
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        }
    }
    @Provides
    @Singleton
    fun provideOkHttpClient() =
        OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()

    @Provides
    @Singleton
    fun provideRetrofitService(okHttpClient: OkHttpClient): ApiService =
        Retrofit.Builder()
            .baseUrl(ApiService.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(ApiService::class.java)

    @Provides
    @Singleton
    fun provideQuestionsRepository(apiService: ApiService): QuestionsRepository =
        QuestionsRepositoryImpl(apiService)
}