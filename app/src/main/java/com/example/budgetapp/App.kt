package com.example.budgetapp

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

@SuppressLint("StaticFieldLeak")
class App: Application() {

    override fun onCreate() {
        super.onCreate()
        mContext = this
    }

    companion object{
        lateinit var mContext: Context
            private set
    }
}