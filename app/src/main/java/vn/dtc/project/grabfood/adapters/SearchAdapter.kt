package vn.dtc.project.grabfood.adapters

import android.annotation.SuppressLint
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import vn.dtc.project.grabfood.data.Food
import vn.dtc.project.grabfood.databinding.FoodRvItemBinding
import vn.dtc.project.grabfood.databinding.SearchRvItemBinding

class SearchAdapter: RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {
    inner class SearchViewHolder(private val binding: SearchRvItemBinding): RecyclerView.ViewHolder(binding.root){
        @SuppressLint("SetTextI18n")
        fun bind(food: Food){
            binding.apply {
                Glide.with(itemView).load(food.images[0]).into(imgSearchItem)
                food.offerPercentage?.let {
                    val remainingPricePercentage = 1f - it
                    val priceAfterOffer = remainingPricePercentage * food.price
                    tvNewPrice.text = "$ ${String.format("%.2f", priceAfterOffer)}"
                    tvOldPrice.paintFlags= Paint.STRIKE_THRU_TEXT_FLAG
                }
                if (food.offerPercentage == null)
                    tvNewPrice.visibility = View.INVISIBLE
                tvOldPrice.text = "$ ${food.price}"
                tvFoodName.text = food.name
            }
        }
    }
    private val diffCallback = object : DiffUtil.ItemCallback<Food>() {
        override fun areItemsTheSame(oldItem: Food, newItem: Food): Boolean {
            return oldItem.id == newItem.id

        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Food, newItem: Food): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return SearchViewHolder(
            SearchRvItemBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }
    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val food = differ.currentList[position]
        holder.bind(food)

        holder.itemView.setOnClickListener{
            onClick?.invoke(food)
        }
    }
    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    var onClick:((Food) -> Unit)? = null
}