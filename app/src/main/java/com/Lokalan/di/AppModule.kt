package com.Lokalan.di


import com.Lokalan.data.repository.AuthRepository
import com.Lokalan.data.repository.BudayaRepository
import com.Lokalan.data.repository.KategoriBudayaRepository
import com.Lokalan.data.repository.ReviewRepository
import com.Lokalan.data.service.ApiService
import com.Lokalan.data.service.BudayaService
import com.Lokalan.data.service.KategoriService
import com.Lokalan.data.service.ReviewService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // Sesuaikan dengan format yang benar
    private const val BASE_URL = "http://10.0.2.2:5002/api/"

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        // Buat HttpLoggingInterceptor
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY // Menampilkan body request dan response
        }

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideBudayaService(retrofit: Retrofit): BudayaService {
        return retrofit.create(BudayaService::class.java)
    }

    @Provides
    @Singleton
    fun provideKategoriService(retrofit: Retrofit): KategoriService {
        return retrofit.create(KategoriService::class.java)
    }

    @Provides
    @Singleton
    fun provideReviewService(retrofit: Retrofit): ReviewService {
        return retrofit.create(ReviewService::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(service: ApiService): AuthRepository {
        return AuthRepository(service)
    }

    @Provides
    @Singleton
    fun provideBudayaRepository(service: BudayaService): BudayaRepository {
        return BudayaRepository(service)
    }

    @Provides
    @Singleton
    fun provideKategoriBudayaRepository(service: KategoriService): KategoriBudayaRepository {
        return KategoriBudayaRepository(service)
    }

    @Provides
    @Singleton
    fun provideReviewRepository(service: ReviewService): ReviewRepository {
        return ReviewRepository(service)
    }
}