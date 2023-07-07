package com.example.dishdiscovery.adapters
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dishdiscovery.databinding.CategoriesBinding
import com.example.dishdiscovery.services.Category

class CategoriesAdapter : RecyclerView.Adapter<CategoriesAdapter.CategoriesViewHolder>() {
    private var categoriesList = ArrayList<Category>()
    lateinit var onItemClick :((Category) -> Unit)


    fun setCategory(categoriesList: ArrayList<Category>) {
        this.categoriesList = categoriesList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder {
        return CategoriesViewHolder(
            CategoriesBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    override fun getItemCount(): Int {
        return categoriesList.size
    }

    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) {
        Glide.with(holder.itemView).load(categoriesList[position].strCategoryThumb)
            .into(holder.binding.categoryImg)
        holder.binding.categoryTxt.text = categoriesList[position].strCategory

        holder.itemView.setOnClickListener{
            onItemClick.invoke(categoriesList[position])
        }
    }

    class CategoriesViewHolder(val binding: CategoriesBinding) :
        RecyclerView.ViewHolder(binding.root)
}