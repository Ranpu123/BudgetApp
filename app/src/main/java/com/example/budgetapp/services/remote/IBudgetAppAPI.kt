package com.example.budgetapp.services.remote


import com.example.budgetapp.domain.models.expense.Expense
import com.example.budgetapp.domain.models.expense.FixedExpense
import com.example.budgetapp.domain.models.income.FixedIncome
import com.example.budgetapp.domain.models.income.Income
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import java.time.LocalDate

interface IBudgetAppAPI {

    //Expenses
    @GET(EXPENSES)
    suspend fun getExpenses(@Query("userId") userId: Int): Response<List<Expense>>

    @POST(EXPENSES)
    suspend fun addExpense(@Body expense: Expense): Response<Expense>

    @DELETE(EXPENSES + PATH_ID)
    suspend fun removeExpense(@Path(value = "id") expenseId: String): Response<Expense>


    //Incomes
    @GET(INCOMES)
    suspend fun getIncomes(@Query("userId") userId: Int): Response<List<Income>>

    @POST(INCOMES)
    suspend fun addIncome(@Body income: Income): Response<Income>

    @DELETE(INCOMES + PATH_ID)
    suspend fun removeIncome(@Path(value = "id") incomeId: String): Response<Income>


    //FixedIncomes
    @GET(FIXED_EXPENSES)
    suspend fun getFixedExpense(@Query("userId") userId: Int): Response<List<FixedExpense>>

    @POST(FIXED_EXPENSES)
    suspend fun addFixedExpense(@Body fixedIncome: FixedIncome): Response<FixedExpense>

    @DELETE(FIXED_EXPENSES + PATH_ID)
    suspend fun removeFixedExpense(@Path(value = "id") fixedIncomeId: String): Response<FixedExpense>

    @PATCH(FIXED_EXPENSES + PATH_ID)
    suspend fun updateFixedExpenseLastDate(@Body lastDate: LocalDate): Response<FixedExpense>


    //FixedIncomes
    @GET(FIXED_INCOMES)
    suspend fun getFixedIncomes(@Query("userId") userId: Int): Response<List<FixedIncome>>

    @POST(FIXED_INCOMES)
    suspend fun addFixedIncome(@Body fixedIncome: FixedIncome): Response<FixedIncome>

    @DELETE(FIXED_INCOMES + PATH_ID)
    suspend fun removeFixedIncome(@Path(value = "id") fixedIncomeId: String): Response<FixedIncome>

    @PATCH(FIXED_INCOMES + PATH_ID)
    suspend fun updateFixedIncomeLastDate(@Body lastDate: LocalDate): Response<FixedIncome>

    companion object{
        const val EXPENSES = "/expenses"
        const val INCOMES = "/incomes"
        const val FIXED_EXPENSES = "/fixed_expenses"
        const val FIXED_INCOMES = "/fixed_incomes"
        const val PATH_ID = "/{id}"

    }
}