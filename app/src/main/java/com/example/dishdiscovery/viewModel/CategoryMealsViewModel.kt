package com.example.dishdiscovery.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dishdiscovery.retrofit.RetrofitInstance
import com.example.dishdiscovery.services.MealsByCategoryList
import com.example.dishdiscovery.services.MealsByCategory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryMealsViewModel: ViewModel() {
    private var categoryMealsLiveData = MutableLiveData<List<MealsByCategory>>()

    fun getCategoryMeals(mealName : String ) {
        RetrofitInstance.api.getCategoryMeals(mealName).enqueue(object : Callback<MealsByCategoryList> {
            override fun onResponse(call: Call<MealsByCategoryList>, response: Response<MealsByCategoryList>) {
                if (response.body() != null) {
                    categoryMealsLiveData.value = response.body()!!.meals
                    Log.d("category meals",  "category meals loaded")

                }

            }

            override fun onFailure(call: Call<MealsByCategoryList>, t: Throwable) {
                Log.d("category Meals Error", t.message.toString())

            }
        })
    }

    fun observerCategoryMealsLiveData(): LiveData<List<MealsByCategory>> {
        return categoryMealsLiveData
    }
}