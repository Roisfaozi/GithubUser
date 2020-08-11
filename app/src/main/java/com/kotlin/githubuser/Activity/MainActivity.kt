package com.kotlin.githubuser.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.kotlin.githubuser.Adapter.UserAdapter
import com.kotlin.githubuser.Data.User
import com.kotlin.githubuser.R
import com.kotlin.githubuser.ViewModel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    private lateinit var userAdapter: UserAdapter
    private lateinit var mainViewModel: MainViewModel

    private var users : ArrayList<User> = ArrayList()

    companion object {
        val TAG = MainActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)
        userAdapter = UserAdapter()

        showRecyclerView()
        getDataUser()
        getUserSearch()

        mainViewModel(userAdapter)
    }

    private fun mainViewModel(adapter: UserAdapter){
        mainViewModel.getListUsers().observe(this, Observer { listUser ->
            if ( listUser != null) {
                adapter.setData(listUser)
                showLoading(false)
            }
        })
    }

    private fun getDataUser() {
        mainViewModel.getDataUser(applicationContext)
        showLoading(true)
    }

    private fun getUserSearch() {
        user_search.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String): Boolean {
                if (query.isNotEmpty()) {
                    users.clear()
                    showRecyclerView()
                    mainViewModel.getUserSearch(query, applicationContext)
                    showLoading(true)
                    mainViewModel(userAdapter)
                } else {
                    return true
                }
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
    }


    private fun showRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        rv_user.setLayoutManager(layoutManager)
        rv_user.setHasFixedSize(true)

        userAdapter.notifyDataSetChanged()
        rv_user.adapter= userAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.language_setting) {
            val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(mIntent)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.INVISIBLE
        }
    }


}