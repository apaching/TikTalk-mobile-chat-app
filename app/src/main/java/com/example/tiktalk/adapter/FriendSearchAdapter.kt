package com.example.tiktalk.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tiktalk.databinding.EachUserBinding
import com.example.tiktalk.model.UserInfoModel

class FriendSearchAdapter(val context : Context, val resultsList : ArrayList<UserInfoModel>?) : RecyclerView.Adapter<FriendSearchAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : FriendSearchAdapter.ViewHolder {
        return ViewHolder(EachUserBinding.inflate(LayoutInflater.from(parent.context), parent, false), context)
    }

    override fun onBindViewHolder(holder : FriendSearchAdapter.ViewHolder, position: Int) {
        resultsList?.get(position).let {
            if (it != null){
                holder.bind(it, position)
            }
        }
    }

    override fun getItemCount() : Int = this.resultsList?.size!!

    class ViewHolder(val binding : EachUserBinding, val context : Context) : RecyclerView.ViewHolder(binding.root) {
        fun bind(result : UserInfoModel, position : Int) {
            binding.tvUsername.text = result.name
        }
    }
}