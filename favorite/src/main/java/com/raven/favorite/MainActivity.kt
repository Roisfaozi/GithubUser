package com.raven.favorite

import android.database.ContentObserver
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.kotlin.githubuser.data.User
import com.kotlin.githubuser.db.DatabaseContract.FavoriteColumns.Companion.CONTENT_URI
import com.kotlin.githubuser.helper.MappingHelper
import com.raven.favorite.adapter.FavoritAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"
    }

    private lateinit var adapter: FavoritAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        rv_user.layoutManager = LinearLayoutManager(this)
        rv_user.setHasFixedSize(true)
        adapter= FavoritAdapter()

        adapter.notifyDataSetChanged()
        rv_user.adapter= adapter


        val handleThread = HandlerThread("DataObserver")
        handleThread.start()
        val handler = Handler(handleThread.looper)
        val mObserver = object : ContentObserver(handler){
            override fun onChange(selfChange: Boolean) {
                loadData()
            }
        }

        contentResolver.registerContentObserver(CONTENT_URI, true, mObserver)

        if (savedInstanceState == null){
            loadData()
        } else {
            val fav = savedInstanceState.getParcelableArrayList<User>(EXTRA_STATE)
            if (fav != null){
                adapter.listFav = fav
            }
        }

    }

    private fun loadData(){
        GlobalScope.launch (Dispatchers.Main){
            progressBar.visibility = View.VISIBLE
            val deferredFav = async(Dispatchers.IO) {
                val cursor = contentResolver.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }

            val dataFav = deferredFav.await()
            progressBar.visibility = View.INVISIBLE
            if(dataFav.size > 0){
                adapter.listFav = dataFav
            } else{
                adapter.listFav = ArrayList()
                showMessage()
            }
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.listFav)
    }





    private fun showMessage() {
        Snackbar.make(rv_user, "Data Kosong", Snackbar.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

}