package com.example.dishdiscovery.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.dishdiscovery.activities.CategoryMealsActivity
import com.example.dishdiscovery.activities.MainActivity
import com.example.dishdiscovery.adapters.CategoriesAdapter
import com.example.dishdiscovery.databinding.FragmentCategoriesBinding
import com.example.dishdiscovery.services.Category
import com.example.dishdiscovery.viewModel.HomeViewModel


class CategoriesFragment : Fragment() {
    private lateinit var binding: FragmentCategoriesBinding
    private lateinit var homeMvvm: HomeViewModel
    private lateinit var categoriesAdapter: CategoriesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View{
        binding = FragmentCategoriesBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        homeMvvm = (activity as MainActivity).viewModel
        categoriesAdapter = CategoriesAdapter()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)

        prepareCategoriesRecyclerView()
        homeMvvm.getCategories()
        observerCategories()
        onCategoryClick()
    }
    private fun prepareCategoriesRecyclerView(){
        binding.categoriesRecycler.apply {
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
            intent.putExtra(HomeFragment.CATEGORY, category.strCategory)
            startActivity(intent)
        }
    }


}