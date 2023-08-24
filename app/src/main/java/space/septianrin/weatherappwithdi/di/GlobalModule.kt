package space.septianrin.weatherappwithdi.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import space.septianrin.weatherappwithdi.BuildConfig
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GlobalModule {

    @Provides
    @Singleton
    fun providesApiKey(): String {
        return BuildConfig.WEATHER_API_KEY
    }

}