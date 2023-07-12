package com.example.dishdiscovery.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.dishdiscovery.activities.MainActivity
import com.example.dishdiscovery.activities.MealActivity
import com.example.dishdiscovery.adapters.MealsAdapter
import com.example.dishdiscovery.databinding.FragmentSearchBinding
import com.example.dishdiscovery.viewModel.HomeViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private lateinit var homeMvvm: HomeViewModel
    private lateinit var searchRecyclerViewAdapter: MealsAdapter


    companion object {
        const val MEAL_ID = "com.example.dishdiscovery.fragments.idMeal"
        const val MEAL_NAME = "com.example.dishdiscovery.fragments.nameMeal"
        const val MEAL_THUMB = "com.example.dishdiscovery.fragments.thumbMeal"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeMvvm = (activity as MainActivity).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareSearchRecyclerView()
        observeSearchedMealsLiveData()
        onMealClick()

        var searchJob: Job? = null
        binding.searchEditTxt.addTextChangedListener { searchQuery ->
            searchJob?.cancel()
            searchJob = lifecycleScope.launch {
                delay(500)
                homeMvvm.searchMeals(searchQuery.toString())
            }
        }
    }

    private fun prepareSearchRecyclerView() {
        searchRecyclerViewAdapter = MealsAdapter()
        binding.searchRv.apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            adapter = searchRecyclerViewAdapter
        }
    }

    private fun observeSearchedMealsLiveData() {
        homeMvvm.observerSearchedMealsLiveData().observe(
            viewLifecycleOwner
        ) { mealsList ->
            searchRecyclerViewAdapter.differ.submitList(mealsList)
        }
    }

    private fun onMealClick() {
        searchRecyclerViewAdapter.onItemClick = { meal ->
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(MEAL_ID, meal.idMeal)
            intent.putExtra(MEAL_NAME, meal.strMeal)
            intent.putExtra(MEAL_THUMB, meal.strMealThumb)
            startActivity(intent)
        }
    }


}
