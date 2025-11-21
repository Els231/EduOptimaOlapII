package com.example.eduoptimaolapii.di

import com.example.eduoptimaolapii.BuildConfig
import com.example.eduoptimaolapii.data.remote.api.mongodb.MongoAuthService
import com.example.eduoptimaolapii.data.remote.api.mongodb.MongoEventoService
import com.example.eduoptimaolapii.data.remote.api.mongodb.MongoMatriculaService
import com.example.eduoptimaolapii.data.remote.api.mongodb.MongoNotaService
import com.example.eduoptimaolapii.data.remote.api.mongodb.MongoEstudianteService
import com.example.eduoptimaolapii.data.remote.api.olap.DashboardService
import com.example.eduoptimaolapii.data.remote.api.olap.ReportesService
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import com.example.eduoptimaolapii.data.remote.api.olap.OLAPQueryService
import com.example.eduoptimaolapii.data.repository.AuthRepository
import com.example.eduoptimaolapii.data.repository.DashboardRepository
import com.example.eduoptimaolapii.data.repository.EstudianteRepository
import com.example.eduoptimaolapii.data.repository.MongoDBRepository
import com.example.eduoptimaolapii.data.repository.MatriculaRepository
import com.example.eduoptimaolapii.data.repository.NotaRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

// QUALIFIERS para diferenciar los Retrofit
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MongoDBRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class OLAPRetrofit

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
                val request = chain.request()
                println("🌐 REQUEST: ${request.method} ${request.url}")

                try {
                    val response = chain.proceed(request)
                    println("📡 RESPONSE: ${response.code} ${response.message}")

                    when (response.code) {
                        200 -> println("✅ SUCCESS")
                        404 -> println("❌ ENDPOINT NO ENCONTRADO: ${request.url}")
                        500 -> println("❌ ERROR SERVIDOR")
                        else -> println("⚠️ CÓDIGO: ${response.code}")
                    }

                    response
                } catch (e: Exception) {
                    println("💥 NETWORK ERROR: ${e.message}")
                    throw e
                }
            }
            .build()
    }
// En AppModule.kt - REEMPLAZA estas líneas:

    @Provides
    @Singleton
    @MongoDBRetrofit
    fun provideMongoDBRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.MONGODB_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(SimpleXmlConverterFactory.create()) // ✅ Cambiar a XML
            .build()
    }

    @Provides
    @Singleton
    @OLAPRetrofit
    fun provideOLAPRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.OLAP_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(SimpleXmlConverterFactory.create()) // ✅ Cambiar a XML
            .build()
    }

    // SERVICIOS MONGODB - Usan @MongoDBRetrofit
    @Provides
    @Singleton
    fun provideMongoAuthService(@MongoDBRetrofit retrofit: Retrofit): MongoAuthService {
        return retrofit.create(MongoAuthService::class.java)
    }

    @Provides
    @Singleton
    fun provideMongoEventoService(@MongoDBRetrofit retrofit: Retrofit): MongoEventoService {
        return retrofit.create(MongoEventoService::class.java)
    }

    @Provides
    @Singleton
    fun provideMongoEstudianteService(@MongoDBRetrofit retrofit: Retrofit): MongoEstudianteService {
        return retrofit.create(MongoEstudianteService::class.java)
    }

    @Provides
    @Singleton
    fun provideMongoMatriculaService(@MongoDBRetrofit retrofit: Retrofit): MongoMatriculaService {
        return retrofit.create(MongoMatriculaService::class.java)
    }

    @Provides
    @Singleton
    fun provideMongoNotaService(@MongoDBRetrofit retrofit: Retrofit): MongoNotaService {
        return retrofit.create(MongoNotaService::class.java)
    }

    // SERVICIOS OLAP - Usan @OLAPRetrofit
    @Provides
    @Singleton
    fun provideDashboardService(@OLAPRetrofit retrofit: Retrofit): DashboardService {
        return retrofit.create(DashboardService::class.java)
    }

    @Provides
    @Singleton
    fun provideReportesService(@OLAPRetrofit retrofit: Retrofit): ReportesService {
        return retrofit.create(ReportesService::class.java)
    }

    // NUEVO SERVICIO OLAPQueryService - Usa @OLAPRetrofit
    @Provides
    @Singleton
    fun provideOLAPQueryService(@OLAPRetrofit retrofit: Retrofit): OLAPQueryService {
        return retrofit.create(OLAPQueryService::class.java)
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
        dashboardService: DashboardService,
        olapQueryService: OLAPQueryService
    ): DashboardRepository {
        return DashboardRepository(dashboardService, olapQueryService)  // PASA AMBOS PARÁMETROS
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
