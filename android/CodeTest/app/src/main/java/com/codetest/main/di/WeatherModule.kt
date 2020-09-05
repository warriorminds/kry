package com.codetest.main.di

import androidx.lifecycle.ViewModel
import com.codetest.main.KeyUtil
import com.codetest.main.WeatherForecastActivity
import com.codetest.main.api.LocationApiService
import com.codetest.main.repositories.LocationsRepository
import com.codetest.main.repositories.LocationsRepositoryImpl
import com.codetest.main.viewmodels.ViewModelKey
import com.codetest.main.viewmodels.LocationsViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
abstract class WeatherModule {
    @Module
    companion object {
        @Provides
        fun providesOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().also { it.level = HttpLoggingInterceptor.Level.BODY })
            .addInterceptor { chain ->
                val originalRequest = chain.request()
                val request = originalRequest.newBuilder()
                    .header("X-Api-Key", KeyUtil().getKey())
                    .method(originalRequest.method, originalRequest.body)
                    .build()

                chain.proceed(request)
            }
            .build()

        @Provides
        fun providesRetrofit(okHttpClient: OkHttpClient): Retrofit =
            Retrofit.Builder()
                .baseUrl("https://app-code-test.kry.pet/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient).build()

        @Provides
        fun providesRetrofitService(retrofit: Retrofit): LocationApiService =
            retrofit.create(LocationApiService::class.java)
    }

    @ContributesAndroidInjector
    abstract fun weatherActivity(): WeatherForecastActivity

    @Binds
    @IntoMap
    @ViewModelKey(LocationsViewModel::class)
    abstract fun bindWeatherViewModel(locationsViewModel: LocationsViewModel): ViewModel

    @Binds
    abstract fun weatherRepository(weatherRepository: LocationsRepositoryImpl): LocationsRepository
}