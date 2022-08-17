package com.crystal.cardlistview

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.crystal.cardlistview.databinding.ItemCardBinding

class CardAdapter(data: MutableList<Store> = mutableListOf()) :
    RecyclerView.Adapter<CardAdapter.CardViewHolder>() {
    private var dataList: MutableList<Store>

    init {
        dataList = data
    }

    fun submitList(list: MutableList<Store>) {
        dataList = list
        notifyDataSetChanged()
    }

    inner class CardViewHolder(private val binding: ItemCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(store: Store){
            binding.store = store
            val range = (0..0xffffff)
            val randomColor = "%06x".format(range.random())
            Log.d("CardViewHolder", " randomColor : ${randomColor}")

            binding.cardView.setCardBackgroundColor(Color.parseColor(("#${randomColor}")))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        return CardViewHolder(
            ItemCardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemId(position: Int): Long {
        return if (dataList.isNotEmpty()) {
            super.getItemId(position % dataList.size)
        } else {
            super.getItemId(position)
        }
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
//        holder.bind(dataList[position])
        if(dataList.isNotEmpty()){
            holder.bind(dataList[position % dataList.size])
        }
    }

    override fun getItemCount(): Int {
        return if(dataList.isNotEmpty()) Int.MAX_VALUE else 0
    }
}