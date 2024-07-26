package com.example.budgetapp

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner

class TestBudgetRunner: AndroidJUnitRunner() {

    override fun newApplication(
        cl: ClassLoader?,
        className: String?,
        context: Context?
    ): Application {
        return super.newApplication(cl, TestBudgetApp::class.java.name, context)
    }

}