package com.beatareka.jwtlogin.module;

import android.content.Context
import com.beatareka.jwtlogin.model.UserMapper
import com.beatareka.jwtlogin.network.UserAPI
import com.beatareka.jwtlogin.repository.TokenStoreRepository
import com.beatareka.jwtlogin.repository.TokenStoreRepositoryImpl
import com.beatareka.jwtlogin.repository.UserRepository
import com.beatareka.jwtlogin.repository.UserRepositoryImpl
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    private val BASE_URL = "https://example.vividmindsoft.com"

    @Singleton
    @Provides
    fun provideDataStoreRepository(
        @ApplicationContext app: Context
    ): TokenStoreRepository = TokenStoreRepositoryImpl(app)


    @Singleton
    @Provides
    fun provideAPIRepository(
        userAPI: UserAPI,
        dataStoreRepository: TokenStoreRepository
    ): UserRepository = UserRepositoryImpl(userAPI, dataStoreRepository)

    @Singleton
    @Provides
    fun providesRetrofitBuilder(): Retrofit.Builder {
        return Retrofit.Builder().baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor()
                .apply { this.level = HttpLoggingInterceptor.Level.BODY })
            .build()
    }

    @Singleton
    @Provides
    fun providesRetrofit(retrofitBuilder: Retrofit.Builder, okHttpClient: OkHttpClient): Retrofit {
        return retrofitBuilder
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    fun providesUserAPI(retrofit: Retrofit): UserAPI {
        return retrofit.create(UserAPI::class.java)
    }

    @Singleton
    @Provides
    fun providesGson(): Gson {
        return Gson()
    }

}
