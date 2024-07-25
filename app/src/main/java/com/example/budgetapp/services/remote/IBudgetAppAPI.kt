package com.example.budgetapp.services.remote


import com.example.budgetapp.domain.models.expense.Expense
import com.example.budgetapp.domain.models.expense.FixedExpense
import com.example.budgetapp.domain.models.income.FixedIncome
import com.example.budgetapp.domain.models.income.Income
import com.example.budgetapp.services.remote.models.ApiExpense
import com.example.budgetapp.services.remote.models.ApiFixedExpense
import com.example.budgetapp.services.remote.models.ApiFixedIncome
import com.example.budgetapp.services.remote.models.ApiIncome
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
    suspend fun addExpense(@Body expense: ApiExpense): Response<Expense>

    @DELETE(EXPENSES + PATH_ID)
    suspend fun removeExpense(@Path(value = "id") expenseId: String): Response<Expense>

    @PATCH(EXPENSES + PATH_ID)
    suspend fun updateExpense(@Path(value = "id") expenseId: String, @Body expense: ApiExpense): Response<Expense>


    //Incomes
    @GET(INCOMES)
    suspend fun getIncomes(@Query("userId") userId: Int): Response<List<Income>>

    @POST(INCOMES)
    suspend fun addIncome(@Body income: ApiIncome): Response<Income>

    @DELETE(INCOMES + PATH_ID)
    suspend fun removeIncome(@Path(value = "id") incomeId: String): Response<Income>

    @PATCH(INCOMES + PATH_ID)
    suspend fun updateIncome(@Path(value = "id") incomeId: String, @Body income: ApiIncome): Response<Income>


    //FixedIncomes
    @GET(FIXED_EXPENSES)
    suspend fun getFixedExpense(@Query("userId") userId: Int): Response<List<FixedExpense>>

    @POST(FIXED_EXPENSES)
    suspend fun addFixedExpense(@Body fixedExpense: ApiFixedExpense): Response<FixedExpense>

    @DELETE(FIXED_EXPENSES + PATH_ID)
    suspend fun removeFixedExpense(@Path(value = "id") fixedExpenseId: String): Response<FixedExpense>

    @PATCH(FIXED_EXPENSES + PATH_ID)
    suspend fun updateFixedExpenseLastDate(@Path(value = "id")fixedExpenseId: String, @Body fixedExpense: ApiFixedExpense): Response<FixedExpense>

    @PATCH(FIXED_EXPENSES + PATH_ID)
    suspend fun updateFixedExpense(@Path(value = "id") fixedExpenseId: String, @Body fixedExpense: ApiFixedExpense): Response<FixedExpense>


    //FixedIncomes
    @GET(FIXED_INCOMES)
    suspend fun getFixedIncomes(@Query("userId") userId: Int): Response<List<FixedIncome>>

    @POST(FIXED_INCOMES)
    suspend fun addFixedIncome(@Body fixedIncome: ApiFixedIncome): Response<FixedIncome>

    @DELETE(FIXED_INCOMES + PATH_ID)
    suspend fun removeFixedIncome(@Path(value = "id") fixedIncomeId: String): Response<FixedIncome>

    @PATCH(FIXED_INCOMES + PATH_ID)
    suspend fun updateFixedIncomeLastDate(@Path(value = "id") fixedIncomeId: String, @Body fixedIncome: ApiFixedIncome): Response<FixedIncome>

    @PATCH(FIXED_INCOMES + PATH_ID)
    suspend fun updateFixedIncome(@Path(value = "id") fixedIncomeId: String, @Body fixedIncome: ApiFixedIncome): Response<FixedIncome>

    companion object{
        const val EXPENSES = "/expenses"
        const val INCOMES = "/incomes"
        const val FIXED_EXPENSES = "/fixed_expenses"
        const val FIXED_INCOMES = "/fixed_incomes"
        const val PATH_ID = "/{id}"

    }
}