package com.example.fluent

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class FluentApp: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@FluentApp)
            modules()
        }
    }
}