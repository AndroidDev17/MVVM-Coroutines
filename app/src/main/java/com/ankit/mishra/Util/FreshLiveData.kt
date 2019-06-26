package com.ankit.mishra.Util

import android.os.SystemClock
import android.util.Log
import androidx.annotation.NonNull
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.MutableLiveData

class FreshLiveData<T>:MutableLiveData<T>() {

    private val TAG="FreshLiveData"
    private var lastEmittedValue:T?=null
    private var lastEmittedTimeStamp=0L
    override fun postValue(value: T) {
        super.postValue(value)
        lastEmittedTimeStamp=SystemClock.currentThreadTimeMillis()
        lastEmittedValue=value
        Log.d(TAG,"postValue--${value.toString()}")
    }

    override fun getValue(): T? {
        return super.getValue()
        SystemClock.currentThreadTimeMillis()
        Log.d(TAG,"postValue--${value.toString()}")
    }




}

class MyLifeCycleregistry(@NonNull provider: LifecycleOwner) :LifecycleRegistry(provider) {
    override fun addObserver(observer: LifecycleObserver) {
        super.addObserver(observer)
    }
}