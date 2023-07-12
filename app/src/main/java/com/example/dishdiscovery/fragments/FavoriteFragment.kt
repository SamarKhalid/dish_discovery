package com.example.dishdiscovery.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.dishdiscovery.activities.MainActivity
import com.example.dishdiscovery.activities.MealActivity
import com.example.dishdiscovery.adapters.MealsAdapter
import com.example.dishdiscovery.databinding.FragmentFavoriteBinding
import com.example.dishdiscovery.viewModel.HomeViewModel
import com.google.android.material.snackbar.Snackbar

class FavoriteFragment : Fragment() {
    private lateinit var binding: FragmentFavoriteBinding
    private lateinit var homeMvvm: HomeViewModel
    private lateinit var mealsAdapter: MealsAdapter


    companion object{
        const val MEAL_ID = "com.example.dishdiscovery.fragments.idMeal"
        const val MEAL_NAME = "com.example.dishdiscovery.fragments.nameMeal"
        const val MEAL_THUMB = "com.example.dishdiscovery.fragments.thumbMeal"
    }

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        homeMvvm = (activity as MainActivity).viewModel
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareCategoriesRecyclerView()
        observeFavorites()
        onMealClick()

        val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder,
            )= true

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                homeMvvm.deleteMeal(mealsAdapter.differ.currentList[position])
                Snackbar.make(binding.root, "Meal removed from favorites", Snackbar.LENGTH_SHORT).show()

            }
        }
        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(binding.favoritesRv)
    }
    private fun prepareCategoriesRecyclerView(){
        mealsAdapter = MealsAdapter()
        binding.favoritesRv.apply {
           layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            adapter = mealsAdapter
        }
    }
    private fun observeFavorites() {
        homeMvvm.observerFavoritesLiveData().observe(
            viewLifecycleOwner
        ) { meals ->
            meals.forEach { _ ->
                mealsAdapter.differ.submitList(meals)
            }
        }
    }

    private fun onMealClick(){
        mealsAdapter.onItemClick= { meal ->
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(MEAL_ID, meal.idMeal)
            intent.putExtra(MEAL_NAME, meal.strMeal)
            intent.putExtra(MEAL_THUMB, meal.strMealThumb)
            startActivity(intent)
        }

    }

}