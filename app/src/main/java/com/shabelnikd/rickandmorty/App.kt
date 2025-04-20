package com.shabelnikd.rickandmorty

import android.app.Application
import com.shabelnikd.rickandmorty.data.core.di.dataModule
import com.shabelnikd.rickandmorty.data.core.di.ktorModule
import com.shabelnikd.rickandmorty.ui.core.di.uiModule
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(
                uiModule, dataModule, ktorModule
            )
        }
    }
}