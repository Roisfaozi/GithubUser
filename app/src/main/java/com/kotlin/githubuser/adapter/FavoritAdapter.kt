package com.kotlin.githubuser.adapter

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.kotlin.githubuser.Activity.DetailActivity
import com.kotlin.githubuser.Data.CustomOnItemClickListener
import com.kotlin.githubuser.Data.User
import com.kotlin.githubuser.R
import kotlinx.android.synthetic.main.item_user.view.*

class FavoritAdapter(private val activity: Activity): RecyclerView.Adapter<FavoritAdapter.FavViewHolder>(){

    val TAG = FavoritAdapter::class.java.simpleName
    var listFav = ArrayList<User>()
    set(listFav){
        if (listFav.size > 0){
            this.listFav.clear()
        }
        this.listFav.addAll(listFav)
        notifyDataSetChanged()
    }

    fun addItem(user:User){
        this.listFav.add(user)
        notifyItemInserted(this.listFav.size -1)
    }

    fun removeItem(position: Int){
        this.listFav.removeAt(position)
        notifyItemRemoved(position)
        notifyItemChanged(position, this.listFav.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavViewHolder {
        val mView = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return FavViewHolder(mView)
    }

    override fun onBindViewHolder(holder: FavViewHolder, position: Int) {
        holder.bind(listFav[position])
    }

    override fun getItemCount(): Int = listFav.size

    inner class FavViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView) {
        fun bind(user: User) {
            with(itemView) {
                Glide.with(itemView.context)
                    .load(user.avatar)
                    .apply(RequestOptions().override(120, 120))
                    .into(img_item_photo)
                tv_item_name.text = user.name
                tv_item_company.text = user.company

                Log.d(TAG, user.toString())

                rvUser.setOnClickListener(
                    CustomOnItemClickListener(adapterPosition,
                        object : CustomOnItemClickListener.OnItemClickCallback {
                            override fun onItemClicked(view: View, position: Int) {
                                val intent = Intent(activity, DetailActivity::class.java)
                                intent.putExtra(DetailActivity.EXTRA_STATE, user)
                                intent.putExtra(DetailActivity.EXTRA_FAV, "favorite")
                                activity.startActivity(intent)
                            }
                        })
                )
            }
        }
    }

}