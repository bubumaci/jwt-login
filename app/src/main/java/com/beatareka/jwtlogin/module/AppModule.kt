package com.beatareka.jwtlogin.module;

import android.content.Context
import com.beatareka.jwtlogin.repository.TokenStoreRepository
import com.beatareka.jwtlogin.repository.TokenStoreRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideDataStoreRepository(
        @ApplicationContext app: Context
    ): TokenStoreRepository = TokenStoreRepositoryImpl(app)

}
