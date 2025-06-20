package com.example.fluent

import android.app.Application
import com.example.fluent.di.NetworkModule
import com.example.fluent.di.RepositoryModule
import com.example.fluent.di.ViewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class FluentApp: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@FluentApp)
            modules(listOf(NetworkModule, RepositoryModule, ViewModelModule))
        }
    }
}