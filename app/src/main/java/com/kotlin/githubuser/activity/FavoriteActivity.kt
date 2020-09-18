package com.kotlin.githubuser.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.kotlin.githubuser.R
import com.kotlin.githubuser.adapter.FavoritAdapter
import com.kotlin.githubuser.db.DatabaseContract.FavoriteColumns.Companion.CONTENT_URI
import com.kotlin.githubuser.helper.MappingHelper
import kotlinx.android.synthetic.main.activity_favorite.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity() {
    
    private lateinit var adapter : FavoritAdapter
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Favorite"
        
        adapter = FavoritAdapter(this)

        GlobalScope.launch(Dispatchers.Main) {
            val defferedFav = async(Dispatchers.IO){
                val cursor = contentResolver?.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }

            val fav = defferedFav.await()
            if (fav.size > 0) {
                adapter.listFav = fav
            } else {
                adapter.listFav = ArrayList()
                showMessage(R.string.empty_favorit)
            }
        }

        showRecycler()
    }

    private fun showRecycler(){
        rv_favorite.adapter = adapter
        rv_favorite.layoutManager = LinearLayoutManager(this)
        rv_favorite.setHasFixedSize(true)
    }

    private fun showMessage(message: Int){
        Snackbar.make(rv_favorite, message, Snackbar.LENGTH_SHORT).show()
    }
}