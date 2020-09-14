package com.kotlin.githubuser.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.kotlin.githubuser.activity.DetailActivity
import com.kotlin.githubuser.data.User
import com.kotlin.githubuser.R
import kotlinx.android.synthetic.main.item_user.view.*

class FollowingAdapter(private val listFollow : ArrayList<User>) :RecyclerView.Adapter<FollowingAdapter.ListViewHolder>(){


    fun setFollow(items: ArrayList<User>){
        listFollow.clear()
        listFollow.addAll(items)
        notifyDataSetChanged()
    }

    inner class ListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(user: User){
            with(itemView){
                tv_item_name.text = user.name
                tv_item_company.text = user.company
                Glide.with(itemView.context)
                    .load(user.avatar)
                    .apply(RequestOptions())
                    .into(img_item_photo)

            }
        }
    }

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): ListViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_user, viewGroup, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int = listFollow.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listFollow[position])

        val user = listFollow[position]
        holder.itemView.setOnClickListener{
            val folowUser = User(
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
            intent.putExtra(DetailActivity.EXTRA_DETAIL, folowUser)
            holder.itemView.context.startActivity(intent)

        }
    }

}
