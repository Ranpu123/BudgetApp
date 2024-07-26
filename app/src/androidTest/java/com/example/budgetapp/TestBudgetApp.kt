package com.example.budgetapp

import android.app.Application
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin

class TestBudgetApp: Application() {

    override fun onCreate() {
        super.onCreate()
    }

    override fun onTerminate() {
        super.onTerminate()
        stopKoin()
    }
}