package com.example.dishdiscovery.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dishdiscovery.db.MealDatabase
import com.example.dishdiscovery.retrofit.RetrofitInstance
import com.example.dishdiscovery.services.Meal
import com.example.dishdiscovery.services.MealList
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MealViewModel(
    private val mealDatabase: MealDatabase
):ViewModel() {
    private var mealLiveData = MutableLiveData<Meal>()


    fun getMealDetails(id: String) {

        RetrofitInstance.api.getMealDetails(id).enqueue(object : Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if (response.body() != null) {
                    mealLiveData.value = response.body()!!.meals[0]
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("Error", t.message.toString())
            }
        })
    }

    fun observerMealDetailsLiveData(): LiveData<Meal> {
        return mealLiveData
    }

    fun insertMeal(meal: Meal){
        viewModelScope.launch {
            mealDatabase.MealDao().upsert(meal)
        }
    }

    fun deleteMeal(meal: Meal){
        viewModelScope.launch {
            mealDatabase.MealDao().delete(meal)
        }
    }

}