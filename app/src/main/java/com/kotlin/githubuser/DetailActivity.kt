package com.kotlin.githubuser

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.activity_detail.tv_item_name
import kotlinx.android.synthetic.main.item_user.*


class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)



        val user = intent.getParcelableExtra<User>("user")

        img_avatar.setImageResource(user.avatar)
        tv_item_name.text = user?.name
        username.text = user?.userName
        company.text = user?.company
        location.text = user?.location
        followers.text = user?.followers
        followings.text = user?.following
        repository.text = user?.repository


    }
}