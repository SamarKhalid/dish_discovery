package com.example.dishdiscovery.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.dishdiscovery.adapters.CategoryMealsAdapter
import com.example.dishdiscovery.databinding.ActivityCategoryMealsBinding
import com.example.dishdiscovery.fragments.HomeFragment
import com.example.dishdiscovery.services.MealsByCategory
import com.example.dishdiscovery.viewModel.CategoryMealsViewModel

class CategoryMealsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCategoryMealsBinding
    private lateinit var categoryMealsMvvm: CategoryMealsViewModel
    private lateinit var categoryMealsAdapter: CategoryMealsAdapter
    private lateinit var categoryName: String

    companion object{
        const val MEAL_ID = "com.example.dishdiscovery.activities.idMeal"
        const val MEAL_NAME = "com.example.dishdiscovery.activities.nameMeal"
        const val MEAL_THUMB = "com.example.dishdiscovery.activities.thumbMeal"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryMealsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prepareRecyclerView()
        categoryMealsMvvm = ViewModelProvider(this)[CategoryMealsViewModel::class.java]

        observerCategoryMeals()
        getCategoryName()
        setCategoryName()

        onMealClick()
    }

    private fun setCategoryName() {
        binding.categoryName.text = categoryName

    }

    private fun prepareRecyclerView() {
        categoryMealsAdapter = CategoryMealsAdapter()
        binding.categoryMealsRv.apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            adapter = categoryMealsAdapter
        }
    }

    private fun observerCategoryMeals() {
        categoryMealsMvvm.observerCategoryMealsLiveData().observe(this, Observer { mealsList ->
            binding.categoryCount.text = mealsList.size.toString()
            categoryMealsAdapter.setMealsList(mealsList as ArrayList<MealsByCategory>)
        })
    }

    private fun getCategoryName() {
        val intent = intent
        categoryName = intent.getStringExtra(HomeFragment.CATEGORY)!!
        categoryMealsMvvm.getCategoryMeals(categoryName)
    }

    private fun onMealClick(){
        categoryMealsAdapter.onItemClick= { meal ->
            val intent = Intent(this,MealActivity::class.java)
            intent.putExtra(MEAL_ID, meal.idMeal)
            intent.putExtra(MEAL_NAME, meal.strMeal)
            intent.putExtra(MEAL_THUMB, meal.strMealThumb)
            startActivity(intent)
        }

    }
}