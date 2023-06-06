package com.example.letgocloneapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.letgocloneapp.Models.ItemData
import com.example.letgocloneapp.databinding.ItemLetgoBinding

class ItemAdapter(var onClickListener: onClickListener):RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {
    var letgoArrayList= ArrayList<ItemData>()
    lateinit var binding: ItemLetgoBinding
    inner class ItemViewHolder(val binding: ItemLetgoBinding):RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val layoutInflater= LayoutInflater.from(parent.context)
        binding= ItemLetgoBinding.inflate(layoutInflater,parent,false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.binding.apply {
            itemName.text= letgoArrayList[position].caption
            val price = letgoArrayList[position].price.toString() + " TL"
            priceItem.text = price
            Glide.with(this.itemImage).load(letgoArrayList[position].imageUrl).into(itemImage)
        }
        holder.itemView.setOnClickListener(
            object : View.OnClickListener{
                override fun onClick(v: View?) {
                    onClickListener.onItemClickListener(holder.adapterPosition,v)
                }
            })
    }
    fun addItem(item : ItemData){
        letgoArrayList.add(item)
        notifyDataSetChanged()
    }
    fun addItems(itemList : List<ItemData>){
        letgoArrayList.clear()
        letgoArrayList.addAll(itemList)
        notifyDataSetChanged()
    }
    override fun getItemCount(): Int {
        return letgoArrayList.size
    }
}