package com.codetest.main.di

import com.codetest.WeatherApplication
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidSupportInjectionModule::class, WeatherModule::class])
interface AppComponent {
    fun inject(app: WeatherApplication)
}