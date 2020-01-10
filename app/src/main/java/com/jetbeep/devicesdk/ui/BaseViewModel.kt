package com.jetbeep.devicesdk.ui

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import org.koin.core.KoinComponent

abstract class BaseViewModel : ViewModel(), KoinComponent {

    protected val disposables = CompositeDisposable()

    protected fun Disposable.track() {
        disposables.add(this)
    }

    abstract fun removeObservers(owner: LifecycleOwner)

    override fun onCleared() {
        disposables.clear()
        super.onCleared()
    }
}