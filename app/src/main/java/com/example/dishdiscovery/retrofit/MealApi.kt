package com.example.dishdiscovery.retrofit

import com.example.dishdiscovery.services.CategoryList
import com.example.dishdiscovery.services.MealList
import com.example.dishdiscovery.services.MealsByCategoryList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MealApi {

    @GET("random.php")
     fun getRandomMeal():Call<MealList>

     @GET("lookup.php?")
     fun getMealDetails(@Query("i") id:String):Call<MealList>

    @GET("filter.php?")
    fun getSuggestedMeals(@Query("c") id:String):Call<MealsByCategoryList>

    @GET("categories.php")
    fun getCategories():Call<CategoryList>

    @GET("filter.php")
    fun getCategoryMeals(@Query("c") id:String):Call<MealsByCategoryList>

    @GET("search.php")
    fun searchMeals(@Query("s") searchQuery:String):Call<MealList>
}