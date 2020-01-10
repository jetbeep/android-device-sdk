package com.jetbeep.devicesdk

import android.app.Application
import com.jetbeep.merchantsdk.MerchantSdk

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        MerchantSdk.init(this)
    }
}