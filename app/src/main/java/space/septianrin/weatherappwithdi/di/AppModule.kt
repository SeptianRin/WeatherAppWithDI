package space.septianrin.weatherappwithdi.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import space.septianrin.weatherappwithdi.BuildConfig
import space.septianrin.weatherappwithdi.model.RxSchedulers
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesApiKey(): String {
        return BuildConfig.WEATHER_API_KEY
    }

    @Provides
    @Named("io")
    fun providesIoScheduler(): Scheduler = Schedulers.io()

    @Provides
    @Named("main")
    fun providesMainScheduler(): Scheduler = AndroidSchedulers.mainThread()

    @Provides
    fun providesRxScheduler(
        @Named("io") ioSchedulers: Scheduler,
        @Named("main") mainSchedulers: Scheduler,
    ): RxSchedulers = RxSchedulers(ioSchedulers, mainSchedulers)

}