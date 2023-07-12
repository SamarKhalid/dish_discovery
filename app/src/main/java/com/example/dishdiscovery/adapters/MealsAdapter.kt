package com.example.dishdiscovery.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dishdiscovery.databinding.MealItemBinding
import com.example.dishdiscovery.services.Meal

class MealsAdapter : RecyclerView.Adapter<MealsAdapter.FavoritesAdapterViewHolder>() {
    lateinit var onItemClick: ((Meal) -> Unit)

    inner class FavoritesAdapterViewHolder(val binding: MealItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    private var diffUtil = object : DiffUtil.ItemCallback<Meal>() {
        override fun areItemsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem.idMeal == newItem.idMeal
        }

        override fun areContentsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem == newItem
        }
    }
     val differ = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesAdapterViewHolder {
        return FavoritesAdapterViewHolder(
            MealItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: FavoritesAdapterViewHolder, position: Int) {
        val meal = differ.currentList[position]
        Glide.with(holder.itemView).load(meal.strMealThumb).into(holder.binding.categoryMealImg)
        holder.binding.categoryTxt.text = meal.strMeal
        holder.itemView.setOnClickListener {
            onItemClick.invoke(differ.currentList[position])
        }
        }

}