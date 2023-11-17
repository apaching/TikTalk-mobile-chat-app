package com.example.tiktalk.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.tiktalk.databinding.EachUserBinding
import com.example.tiktalk.model.UserInfoModel
import com.example.tiktalk.ui.FriendProfileActivity

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
            binding.btnNewMessage.visibility = View.GONE

            if(result.imageUrl != null) {
                Glide.with(context)
                    .load(result.imageUrl)
                    .apply(RequestOptions().centerCrop().override(150, 150))
                    .into(binding.ivUsersImg)
            }

            binding.ll.setOnClickListener {
                val intent = Intent(context, FriendProfileActivity::class.java)
                intent.putExtra("user_info_model", result)
                context.startActivity(intent)
            }
        }
    }
}