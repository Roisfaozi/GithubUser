package com.kotlin.githubuser.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.kotlin.githubuser.Data.User
import com.kotlin.githubuser.Activity.DetailActivity
import com.kotlin.githubuser.R
import kotlinx.android.synthetic.main.item_user.view.*

class UserAdapter : RecyclerView.Adapter<UserAdapter.ListViewHolder>() {
    private val listUser = ArrayList<User>()


    fun setData(items: ArrayList<User>) {
        listUser.clear()
        listUser.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_user, viewGroup, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listUser[position])

        val user = listUser[position]
        holder.itemView.setOnClickListener{
            val userData = User(
                user.name,
                user.userName,
                user.location,
                user.repository,
                user.company,
                user.following,
                user.followers,
                user.avatar
            )
                val intent = Intent(holder.itemView.context, DetailActivity::class.java )
                intent.putExtra(DetailActivity.EXTRA_DETAIL, userData)
                holder.itemView.context.startActivity(intent)

        }
    }

    override fun getItemCount(): Int = listUser.size

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(user: User) {
            with(itemView) {
                Glide.with(itemView.context)
                    .load(user.avatar)
                    .apply(RequestOptions().override(120, 120))
                    .into(img_item_photo)

                tv_item_name.text = user.name
                tv_item_company.text = user.company


            }
        }

    }
}

