package com.raven.favorite.adapter


import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.kotlin.githubuser.data.User
import com.raven.favorite.R
import kotlinx.android.synthetic.main.item_user.view.*

class FavoritAdapter : RecyclerView.Adapter<FavoritAdapter.FavViewHolder>(){

    val TAG = FavoritAdapter::class.java.simpleName
    var listFav = ArrayList<User>()
    set(listFav){
        if (listFav.size > 0){
            this.listFav.clear()
        }
        this.listFav.addAll(listFav)
        notifyDataSetChanged()
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


            }
        }
    }

}