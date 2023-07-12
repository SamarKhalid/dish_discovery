package com.example.dishdiscovery.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.dishdiscovery.R
import com.example.dishdiscovery.activities.CategoryMealsActivity
import com.example.dishdiscovery.activities.MainActivity
import com.example.dishdiscovery.activities.MealActivity
import com.example.dishdiscovery.adapters.CategoriesAdapter
import com.example.dishdiscovery.adapters.SuggestAdapter
import com.example.dishdiscovery.databinding.FragmentHomeBinding
import com.example.dishdiscovery.services.Category
import com.example.dishdiscovery.services.Meal
import com.example.dishdiscovery.services.MealsByCategory
import com.example.dishdiscovery.viewModel.HomeViewModel

class HomeFragment : Fragment(){
    private lateinit var homeBinding: FragmentHomeBinding
    private lateinit var homeMvvm: HomeViewModel
    private lateinit var randomMeal: Meal
    private lateinit var suggestedMealsAdapter: SuggestAdapter
    private lateinit var categoriesAdapter: CategoriesAdapter

    companion object{
        const val MEAL_ID = "com.example.dishdiscovery.fragments.idMeal"
        const val MEAL_NAME = "com.example.dishdiscovery.fragments.nameMeal"
        const val MEAL_THUMB = "com.example.dishdiscovery.fragments.thumbMeal"
        const val CATEGORY = "com.example.dishdiscovery.fragments.category"
    }

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        homeMvvm = (activity as MainActivity).viewModel
        suggestedMealsAdapter = SuggestAdapter()
        categoriesAdapter = CategoriesAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View{
        homeBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return homeBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)

        randomMealLoadingCase()
        prepareSuggestedMealRecyclerView()
        prepareCategoriesRecyclerView()

        homeMvvm.getRandomMeal()
        observerRandomMeal()
        onRandomMealClick()

        homeMvvm.getSuggestedMeals()
        observerSuggestedMeal()
        onSuggestedMealClick()

        homeMvvm.getCategories()
        observerCategories()
        onCategoryClick()

        searchButtonClick()
    }

    private fun searchButtonClick() {
        homeBinding.imgSearch.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
        }
    }

    private fun prepareCategoriesRecyclerView(){
        homeBinding.categoriesRecycler.apply {
            layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
            adapter = categoriesAdapter

        }
    }

    private fun observerCategories(){
        homeMvvm.observerCategoriesLiveData().observe(
            viewLifecycleOwner
        ) { categoriesList ->
            categoriesAdapter.setCategory(categoriesList = categoriesList as ArrayList<Category>)

        }
    }

    private fun onCategoryClick(){
        categoriesAdapter.onItemClick = { category ->
            val intent = Intent(activity, CategoryMealsActivity::class.java)
            intent.putExtra(CATEGORY, category.strCategory)
            startActivity(intent)
        }
    }

    private fun prepareSuggestedMealRecyclerView(){
        homeBinding.suggestRecycler.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = suggestedMealsAdapter

        }
    }

    private fun observerSuggestedMeal(){
        homeMvvm.observerSuggestedMealLiveData().observe(
            viewLifecycleOwner
        ) { mealList ->
            suggestedMealsAdapter.setMeal(mealsList = mealList as ArrayList<MealsByCategory>)
        }
    }

    private fun onSuggestedMealClick(){
        suggestedMealsAdapter.onItemClick = { meal ->
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(MEAL_ID, meal.idMeal)
            intent.putExtra(MEAL_NAME, meal.strMeal)
            intent.putExtra(MEAL_THUMB, meal.strMealThumb)
            startActivity(intent)
        }
    }

    private fun observerRandomMeal(){
        homeMvvm.observerMealLiveData().observe(
            viewLifecycleOwner
        ) { meal ->
            Glide.with(this@HomeFragment).load(meal!!.strMealThumb).into(homeBinding.imgRandomMeal)
            this.randomMeal = meal
            randomMealOnResponseCase()
        }
    }

    private fun onRandomMealClick(){
        homeBinding.cardRandomMeal.setOnClickListener {
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(MEAL_ID, randomMeal.idMeal)
            intent.putExtra(MEAL_NAME, randomMeal.strMeal)
            intent.putExtra(MEAL_THUMB, randomMeal.strMealThumb)
            startActivity(intent)
        }
    }

    private fun randomMealLoadingCase(){
        homeBinding.indicator.visibility = View.VISIBLE
        homeBinding.imgRandomMeal.visibility = View.INVISIBLE
    }

    private fun randomMealOnResponseCase(){
        homeBinding.indicator.visibility = View.INVISIBLE
        homeBinding.imgRandomMeal.visibility = View.VISIBLE
    }
}