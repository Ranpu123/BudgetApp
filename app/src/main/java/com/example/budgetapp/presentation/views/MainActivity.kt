@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.budgetapp.presentation.views

import android.os.Bundle

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.navigation.compose.rememberNavController
import androidx.work.BackoffPolicy
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkRequest

import com.example.budgetapp.domain.modules.ExpenseBottomSheetModule
import com.example.budgetapp.domain.modules.FixedExpenseBottomSheetModule
import com.example.budgetapp.domain.modules.FixedIncomeBottomSheetModule
import com.example.budgetapp.domain.modules.IncomeBottomSheetModule
import com.example.budgetapp.domain.modules.RecordsModule
import com.example.budgetapp.domain.modules.RepositoryModule
import com.example.budgetapp.domain.modules.DatabaseModule
import com.example.budgetapp.domain.modules.HomePageModule
import com.example.budgetapp.domain.modules.RemoteNetworkModule
import com.example.budgetapp.presentation.graphs.RootNavigationGraph
import com.example.budgetapp.presentation.ui.theme.BudgetAppTheme
import com.example.budgetapp.presentation.viewModels.home.HomeViewModel
import com.example.budgetapp.services.remote.IBudgetAppAPI
import com.example.budgetapp.services.workers.DeletePendingWorker
import com.example.budgetapp.services.workers.FetchAllWorker
import com.example.budgetapp.services.workers.StartupWorker
import com.example.budgetapp.services.workers.utils.createOneTimeWorkRequest
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.ext.android.viewModel

import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import java.util.concurrent.TimeUnit


class MainActivity : ComponentActivity() {

    private val homeViewModel: HomeViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startModules()
        enableEdgeToEdge()

        val workManager: WorkManager by inject()

        var data = Data.Builder()
        data.putInt("userId", 1)

        var startupRequest = createOneTimeWorkRequest(data, StartupWorker::class.java)
        var deletePendingRequest = createOneTimeWorkRequest(data, DeletePendingWorker::class.java)
        var fetchAllRequest = createOneTimeWorkRequest(data, FetchAllWorker::class.java)

        workManager
            .beginUniqueWork(
                "sync_job",
                ExistingWorkPolicy.REPLACE,
                deletePendingRequest
            )
            .then(startupRequest)
            .then(fetchAllRequest)
            .enqueue()

        setContent {
            BudgetAppTheme {

                RootNavigationGraph(navController = rememberNavController())

            }
        }
    }

    private fun startModules(){
        stopKoin()
        startKoin(){
            androidLogger()
            androidContext(this@MainActivity)
            modules(
                HomePageModule,
                ExpenseBottomSheetModule,
                IncomeBottomSheetModule,
                FixedExpenseBottomSheetModule,
                FixedIncomeBottomSheetModule,
                RecordsModule,
                DatabaseModule,
                RemoteNetworkModule,
                RepositoryModule
            )
        }
    }
}





