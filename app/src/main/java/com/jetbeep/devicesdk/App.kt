package com.jetbeep.devicesdk

import android.app.Application
import com.jetbeep.devicesdk.di.appModule
import com.jetbeep.devicesdk.di.viewModelModule
import com.jetbeep.merchantsdk.MerchantSdk
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        MerchantSdk.init(this)

        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(listOf(viewModelModule, appModule))
        }
    }
}