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

        username.text = user?.userName
        tv_item_name.text = user?.name
        location.text = user?.location
        repository.text = user?.repository
        company.text = user?.company
        followers.text = user?.followers
        followings.text = user?.following
        img_avatar.setImageResource(user.avatar)


    }
}