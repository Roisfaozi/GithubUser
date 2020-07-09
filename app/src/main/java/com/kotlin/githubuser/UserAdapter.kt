package com.kotlin.githubuser

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import android.transition.Transition as AndroidTransitionTransition

class UserAdapter : RecyclerView.Adapter<UserAdapter.ListViewHolder>() {
    internal var listUser = ArrayList<User>()

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_user, viewGroup, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val user = listUser[position]

        Glide.with(holder.itemView.context)
            .load(user.avatar)
            .apply(RequestOptions().override(120, 120))
            .into(holder.userAvatar)
        holder.userName.text = user.name
        holder.userCompany.text = user.company

        holder.itemView.setOnClickListener{
                val intent = Intent(holder.itemView.context, DetailActivity::class.java )
                intent.putExtra("user",listUser[position])
                holder.itemView.context.startActivity(intent)

        }
    }

    override fun getItemCount(): Int = listUser.size



    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var userName : TextView = itemView.findViewById(R.id.tv_item_name)
        var userCompany : TextView = itemView.findViewById(R.id.tv_item_company)
        var userAvatar  : ImageView = itemView.findViewById(R.id.img_item_photo)

    }
}

