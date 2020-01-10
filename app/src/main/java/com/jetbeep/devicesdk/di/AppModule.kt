package com.jetbeep.devicesdk.di

import com.jetbeep.devicesdk.ui.MainViewModel
import com.jetbeep.merchantsdk.MerchantSdk
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module
import java.util.concurrent.Executor
import java.util.concurrent.Executors

val viewModelModule: Module = module {
    viewModel { MainViewModel() }
}

val appModule: Module = module {
    single { MerchantSdk.bleDevice }
    single<Executor> { Executors.newFixedThreadPool(3) }
}
