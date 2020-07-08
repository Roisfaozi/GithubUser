package com.kotlin.githubuser

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerViewAccessibilityDelegate
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class UserAdapter : RecyclerView.Adapter<UserAdapter.ListViewHolder>() {
    internal var listUser = ArrayList<User>()

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListViewHolder {
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
    }

    override fun getItemCount(): Int = listUser.size



    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var userName : TextView = itemView.findViewById(R.id.tv_item_name)
        var userCompany : TextView = itemView.findViewById(R.id.tv_item_company)
        var userAvatar  : ImageView = itemView.findViewById(R.id.img_item_photo)

    }
}

