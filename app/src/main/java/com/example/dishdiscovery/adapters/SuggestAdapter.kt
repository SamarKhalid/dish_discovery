package com.example.dishdiscovery.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dishdiscovery.databinding.SuggestionsBinding
import com.example.dishdiscovery.services.MealsByCategory

class SuggestAdapter : RecyclerView.Adapter<SuggestAdapter.SuggestedMealViewHolder>() {
    lateinit var onItemClick :((MealsByCategory) -> Unit)
    private var mealsList = ArrayList<MealsByCategory>()

    fun setMeal(mealsList: ArrayList<MealsByCategory>) {
        this.mealsList = mealsList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuggestedMealViewHolder {
        return SuggestedMealViewHolder(
            SuggestionsBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return  mealsList.size
    }

    override fun onBindViewHolder(holder: SuggestedMealViewHolder, position: Int) {
        Glide.with(holder.itemView).load(mealsList[position].strMealThumb)
            .into(holder.binding.suggestionImg)

        holder.itemView.setOnClickListener{
            onItemClick.invoke(mealsList[position])
        }
    }

    class SuggestedMealViewHolder(val binding: SuggestionsBinding) :
        RecyclerView.ViewHolder(binding.root)

}