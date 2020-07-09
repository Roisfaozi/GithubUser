package com.kotlin.githubuser

import android.content.res.TypedArray
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.ListView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var userName: Array<String>
    private lateinit var name: Array<String>
    private lateinit var location: Array<String>
    private lateinit var repository: Array<String>
    private lateinit var company: Array<String>
    private lateinit var followers: Array<String>
    private lateinit var following: Array<String>
    private lateinit var avatar: TypedArray

    private lateinit var userAdapter: UserAdapter

    private var users = arrayListOf<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView: RecyclerView = findViewById(R.id.rv_user)
        userAdapter = UserAdapter()

        recyclerView.adapter = userAdapter

        prepare()
        addItem()
        showRecyclerView()
    }

    private fun prepare() {
        userName = resources.getStringArray(R.array.username)
        name = resources.getStringArray(R.array.name)
        location = resources.getStringArray(R.array.location)
        repository = resources.getStringArray(R.array.repository)
        company = resources.getStringArray(R.array.company)
        followers = resources.getStringArray(R.array.followers)
        following = resources.getStringArray(R.array.following)
        avatar = resources.obtainTypedArray(R.array.avatar)
    }

    private fun addItem() {
        for (position in name.indices) {
            val user = User(
                name[position],
                userName[position],
                avatar.getResourceId(position, -1),
                location[position],
                repository[position],
                followers[position],
                following[position],
                company[position]
            )
            users.add(user)
        }
        userAdapter.listUser = users
    }

    private fun showRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        rv_user?.setLayoutManager(layoutManager)
        rv_user?.setHasFixedSize(true)
    }


}