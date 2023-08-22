package space.septianrin.weatherappwithdi.module.homescreen.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import space.septianrin.weatherappwithdi.module.homescreen.impl.WeatherServiceImpl
import space.septianrin.weatherappwithdi.module.homescreen.service.WeatherService


@Module
@InstallIn(SingletonComponent::class)
object WeatherModule {

    @Provides
    fun provideWeatherService(): WeatherService {
        return WeatherServiceImpl()
    }
}