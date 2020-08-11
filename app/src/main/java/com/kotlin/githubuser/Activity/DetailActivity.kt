package com.kotlin.githubuser.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.kotlin.githubuser.Adapter.SectionPagerAdapter
import com.kotlin.githubuser.Data.User
import com.kotlin.githubuser.R
import kotlinx.android.synthetic.main.activity_detail.*



class DetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_DETAIL = "extra_detail"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        setUser()
        sectionPage()

    }

    private fun setUser(){
        val user = intent.getParcelableExtra<User>(EXTRA_DETAIL)

        tv_name.text = user.name
        username.text = user.userName
        location.text = user.location
        repository.text = user.repository
        company.text = user.company
        followings.text = user.following
        followers.text = user.followers
        Glide.with(this)
            .load(user.avatar)
            .apply(RequestOptions().override(120, 120))
            .into(img_avatar)
    }

    private fun sectionPage(){
        val sectionPagerAdapter = SectionPagerAdapter(this, supportFragmentManager)
        view_pager.adapter = sectionPagerAdapter
        tabs.setupWithViewPager(view_pager)
        supportActionBar?.elevation = 0f
    }
}