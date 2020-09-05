package com.codetest

import android.app.Application
import android.content.Context
import com.codetest.main.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class WeatherApplication : Application(), HasAndroidInjector {

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    companion object {
        private var context: Context? = null
        fun appContext(): Context? {
            return WeatherApplication.context
        }
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext

        DaggerAppComponent.create().also {
            it.inject(this)
        }
    }

    override fun androidInjector(): AndroidInjector<Any> = androidInjector
}
