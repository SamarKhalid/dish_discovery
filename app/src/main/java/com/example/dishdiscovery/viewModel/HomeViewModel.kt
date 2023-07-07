package com.example.dishdiscovery.viewModel
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dishdiscovery.retrofit.RetrofitInstance
import com.example.dishdiscovery.services.Category
import com.example.dishdiscovery.services.CategoryList
import com.example.dishdiscovery.services.Meal
import com.example.dishdiscovery.services.MealList
import com.example.dishdiscovery.services.MealsByCategoryList
import com.example.dishdiscovery.services.MealsByCategory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {
    private var randomMealLiveData = MutableLiveData<Meal>()
    private var suggestedMealLiveData = MutableLiveData<List<MealsByCategory>>()
    private var categoriesLiveData = MutableLiveData<List<Category>>()

    fun getRandomMeal() {
        RetrofitInstance.api.getRandomMeal().enqueue(object : Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if (response.body() != null) {
                    val randomMeal: Meal = response.body()!!.meals[0]
                    randomMealLiveData.value = randomMeal
                    Log.d("Test", "name ${randomMeal.strMeal} img ${randomMeal.strMealThumb}")
                }
            }
            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("Error", t.message.toString())
            }
        })
    }

    fun observerMealLiveData(): LiveData<Meal> {
        return randomMealLiveData

    }

    fun getSuggestedMeals() {
        RetrofitInstance.api.getSuggestedMeals("Seafood").enqueue(object : Callback<MealsByCategoryList> {
            override fun onResponse(call: Call<MealsByCategoryList>, response: Response<MealsByCategoryList>) {
                if (response.body() != null) {
                    suggestedMealLiveData.value = response.body()!!.meals
                }

            }

            override fun onFailure(call: Call<MealsByCategoryList>, t: Throwable) {
                Log.d("Suggest Error", t.message.toString())

            }
        })
    }

    fun observerSuggestedMealLiveData(): LiveData<List<MealsByCategory>> {
        return suggestedMealLiveData
    }

    fun getCategories() {
        RetrofitInstance.api.getCategories().enqueue(object : Callback<CategoryList> {
            override fun onResponse(call: Call<CategoryList>, response: Response<CategoryList>) {
                if (response.body() != null) {
                    categoriesLiveData.value = response.body()!!.categories
                }
            }
            override fun onFailure(call: Call<CategoryList>, t: Throwable) {
                Log.d("Categories Error", t.message.toString())
            }

        })
    }

    fun observerCategoriesLiveData() : LiveData<List<Category>> {
        return categoriesLiveData
    }
}