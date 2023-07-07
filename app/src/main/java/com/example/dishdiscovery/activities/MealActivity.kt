package com.example.dishdiscovery.activities
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.viewModelFactory
import com.bumptech.glide.Glide
import com.example.dishdiscovery.R
import com.example.dishdiscovery.databinding.ActivityMealBinding
import com.example.dishdiscovery.db.MealDatabase
import com.example.dishdiscovery.fragments.HomeFragment
import com.example.dishdiscovery.services.Meal
import com.example.dishdiscovery.viewModel.MealViewModel
import com.example.dishdiscovery.viewModel.MealViewModelFactory
import com.google.android.material.snackbar.Snackbar

class MealActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMealBinding
    private lateinit var mealDetailsMvvm: MealViewModel
    private lateinit var mealId: String
    private lateinit var mealName: String
    private lateinit var mealThumb: String
    private lateinit var youtubeLink: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMealBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mealDatabase = MealDatabase.getInstance(this)
        val viewModelFactory = MealViewModelFactory(mealDatabase)
        mealDetailsMvvm = ViewModelProvider(this, viewModelFactory)[MealViewModel::class.java]

        getMealInformation()
        setInformationIntoViews()
        loadingCase()

        mealDetailsMvvm.getMealDetails(mealId)
        observerMealDetails()
        onYoutubeClick()
        onFavoriteClick()
    }

    private fun onFavoriteClick() {
        binding.favoriteBtn.setOnClickListener {
            mealToSave?.let {
                mealDetailsMvvm.insertMeal(it)
                Snackbar.make(binding.root, "Meal saved", Snackbar.LENGTH_SHORT).show()
                Log.d("NOT ERROR", "Meal Saved")
            }

        }
    }

    private fun onYoutubeClick() {
        binding.imgYoutube.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeLink))
            startActivity(intent)
        }
    }

    private var mealToSave : Meal? = null
    private fun observerMealDetails() {
        mealDetailsMvvm.observerMealDetailsLiveData().observe(this
        ) { t ->
            val meal = t
            mealToSave = meal

            onResponseCase()
            binding.area.text = t!!.strArea

            binding.category.text =
                t.strCategory

            binding.txtInstructionsDetails.text =
                t.strInstructions

            youtubeLink = t.strYoutube!!
        }
    }

    private fun setInformationIntoViews() {
        Glide.with(applicationContext)
            .load(mealThumb).into(binding.imgMealDetail)

        binding.collapsingToolbar.title = mealName
        binding.collapsingToolbar.setExpandedTitleColor(resources.getColor(R.color.white))
    }

    private fun getMealInformation() {
        val intent = intent

        mealId = (intent.getStringExtra(CategoryMealsActivity.MEAL_ID)
            ?: intent.getStringExtra(HomeFragment.MEAL_ID))!!


        mealName = (intent.getStringExtra(CategoryMealsActivity.MEAL_NAME)
            ?: intent.getStringExtra(HomeFragment.MEAL_NAME))!!


        mealThumb = (intent.getStringExtra(CategoryMealsActivity.MEAL_THUMB)
            ?: intent.getStringExtra(HomeFragment.MEAL_THUMB))!!

    }

    private fun loadingCase(){
        binding.indicator.visibility= View.VISIBLE
        binding.category.visibility= View.INVISIBLE
        binding.area.visibility= View.INVISIBLE
        binding.txtInstructions.visibility= View.INVISIBLE
        binding.txtInstructionsDetails.visibility= View.INVISIBLE
        binding.imgYoutube.visibility= View.INVISIBLE
    }

    private fun onResponseCase(){
        binding.indicator.visibility= View.INVISIBLE
        binding.category.visibility= View.VISIBLE
        binding.area.visibility= View.VISIBLE
        binding.txtInstructions.visibility= View.VISIBLE
        binding.txtInstructionsDetails.visibility= View.VISIBLE
        binding.imgYoutube.visibility= View.VISIBLE
    }
}