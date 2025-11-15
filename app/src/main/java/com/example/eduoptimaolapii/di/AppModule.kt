package com.example.eduoptimaolapii.di

import com.example.eduoptimaolapii.BuildConfig
import com.example.eduoptimaolapii.data.remote.api.mongodb.MongoAuthService
import com.example.eduoptimaolapii.data.remote.api.mongodb.MongoEventoService

import com.example.eduoptimaolapii.data.remote.api.mongodb.MongoMatriculaService
import com.example.eduoptimaolapii.data.remote.api.mongodb.MongoNotaService
import com.example.eduoptimaolapii.data.repository.MatriculaRepository
import com.example.eduoptimaolapii.data.repository.NotaRepository

import com.example.eduoptimaolapii.data.remote.api.mongodb.MongoEstudianteService
import com.example.eduoptimaolapii.data.remote.api.olap.DashboardService
import com.example.eduoptimaolapii.data.remote.api.olap.ReportesService
import com.example.eduoptimaolapii.data.repository.AuthRepository
import com.example.eduoptimaolapii.data.repository.DashboardRepository
import com.example.eduoptimaolapii.data.repository.EstudianteRepository
import com.example.eduoptimaolapii.data.repository.MongoDBRepository
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

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = if (BuildConfig.DEBUG) {
                    HttpLoggingInterceptor.Level.BODY
                } else {
                    HttpLoggingInterceptor.Level.NONE
                }
            })
            .addInterceptor { chain ->
                try {
                    val request = chain.request().newBuilder()
                        .addHeader("Content-Type", "application/json")
                        .addHeader("Accept", "application/json")
                        .build()
                    chain.proceed(request)
                } catch (e: Exception) {
                    throw Exception("Error de conexión: Verifique su internet - ${e.message}")
                }
            }
            .addInterceptor { chain ->
                val response = chain.proceed(chain.request())
                if (!response.isSuccessful) {
                    throw when (response.code) {
                        401 -> Exception("No autorizado")
                        404 -> Exception("Recurso no encontrado")
                        500 -> Exception("Error del servidor")
                        else -> Exception("Error ${response.code}: ${response.message}")
                    }
                }
                response
            }
            .build()
    }

    @Provides
    @Singleton
    fun provideMongoDBRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.MONGODB_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideOLAPRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.OLAP_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // SERVICIOS MONGODB
    @Provides
    @Singleton
    fun provideMongoAuthService(retrofit: Retrofit): MongoAuthService {
        return retrofit.create(MongoAuthService::class.java)
    }

    @Provides
    @Singleton
    fun provideMongoEventoService(retrofit: Retrofit): MongoEventoService {
        return retrofit.create(MongoEventoService::class.java)
    }

    @Provides
    @Singleton
    fun provideMongoEstudianteService(retrofit: Retrofit): MongoEstudianteService {
        return retrofit.create(MongoEstudianteService::class.java)
    }

    // SERVICIOS OLAP
    @Provides
    @Singleton
    fun provideDashboardService(retrofit: Retrofit): DashboardService {
        return retrofit.create(DashboardService::class.java)
    }

    @Provides
    @Singleton
    fun provideReportesService(retrofit: Retrofit): ReportesService {
        return retrofit.create(ReportesService::class.java)
    }

    // REPOSITORIOS
    @Provides
    @Singleton
    fun provideAuthRepository(
        mongoAuthService: MongoAuthService
    ): AuthRepository {
        return AuthRepository(mongoAuthService)
    }

    @Provides
    @Singleton
    fun provideDashboardRepository(
        dashboardService: DashboardService
    ): DashboardRepository {
        return DashboardRepository(dashboardService)
    }

    @Provides
    @Singleton
    fun provideMongoDBRepository(
        mongoEventoService: MongoEventoService
    ): MongoDBRepository {
        return MongoDBRepository(mongoEventoService)
    }

    @Provides
    @Singleton
    fun provideEstudianteRepository(
        estudianteService: MongoEstudianteService
    ): EstudianteRepository {
        return EstudianteRepository(estudianteService)
    }

    // SERVICIOS MONGODB ADICIONALES
    @Provides
    @Singleton
    fun provideMongoMatriculaService(retrofit: Retrofit): MongoMatriculaService {
        return retrofit.create(MongoMatriculaService::class.java)
    }

    @Provides
    @Singleton
    fun provideMongoNotaService(retrofit: Retrofit): MongoNotaService {
        return retrofit.create(MongoNotaService::class.java)
    }

    // REPOSITORIOS ADICIONALES
    @Provides
    @Singleton
    fun provideMatriculaRepository(
        matriculaService: MongoMatriculaService
    ): MatriculaRepository {
        return MatriculaRepository(matriculaService)
    }

    @Provides
    @Singleton
    fun provideNotaRepository(
        notaService: MongoNotaService
    ): NotaRepository {
        return NotaRepository(notaService)
    }
}
