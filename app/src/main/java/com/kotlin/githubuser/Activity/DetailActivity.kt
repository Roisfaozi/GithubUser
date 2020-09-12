package com.kotlin.githubuser.Activity

import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.kotlin.githubuser.adapter.SectionPagerAdapter
import com.kotlin.githubuser.Data.User
import com.kotlin.githubuser.R
import com.kotlin.githubuser.db.DatabaseContract
import com.kotlin.githubuser.db.DatabaseContract.FavoriteColumns.Companion.CONTENT_URI
import com.kotlin.githubuser.helper.MappingHelper
import kotlinx.android.synthetic.main.activity_detail.*



class DetailActivity : AppCompatActivity() {

    private var isFavorite = false
    private var menu: Menu? = null
    private var dataUser: User? = null
    private var datafav: User? = null
    private var fromFavorite: String? = null
    private var fromMainActivity: String? = null
    private lateinit var uriWithId: Uri

    companion object {
        internal val TAG = DetailActivity::class.java.simpleName
        const val EXTRA_DETAIL = "extra_detail"
        const val EXTRA_FAV = "extra_fav"
        const val EXTRA_MAIN = "extra_main"
        const val EXTRA_STATE = "extra_state"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        fromFavorite = intent.getStringExtra(EXTRA_FAV)
        fromMainActivity = intent.getStringExtra(EXTRA_MAIN)

        dataUser = intent.getParcelableExtra(EXTRA_STATE) as? User
        Log.d(TAG, "getParcelableExtra: $dataUser")

        uriWithId = Uri.parse(CONTENT_URI.toString() + "/" + dataUser?.id)
        val userFav = contentResolver?.query(uriWithId, null, null, null, null)
        checkFavorite(userFav)

        favorite.setOnClickListener{view ->
            setFavorite()
            favoriteStatus()
        }

        setUser()
        sectionPage()

    }

    private fun setUser(){
        val user = intent.getParcelableExtra<User>(EXTRA_DETAIL)

        tv_name.text = user?.name
        username.text = user?.userName
        location.text = user?.location
        repository.text = user?.repository
        company.text = user?.company
        followings.text = user?.following
        followers.text = user?.followers
        Glide.with(this)
            .load(user?.avatar)
            .apply(RequestOptions().override(120, 120))
            .into(img_avatar)
    }

    private fun sectionPage(){
        val sectionPagerAdapter = SectionPagerAdapter(this, supportFragmentManager)
        view_pager.adapter = sectionPagerAdapter
        tabs.setupWithViewPager(view_pager)
        supportActionBar?.elevation = 0f
    }

    private fun showMessage(message: String){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun setFavorite(){
        isFavorite = false
        if(isFavorite){
           contentResolver.delete(uriWithId, null, null)
            Log.d(TAG, R.string.favorite_removed.toString())
            showMessage("${datafav?.name} ${R.string.favorite_added}")
        } else {
            val values = ContentValues()
            values.put(DatabaseContract.FavoriteColumns._ID, dataUser?.id)
            values.put(DatabaseContract.FavoriteColumns.NAME, dataUser?.name)
            values.put(DatabaseContract.FavoriteColumns.AVATAR, dataUser?.avatar)
            values.put(DatabaseContract.FavoriteColumns.COMPANY, dataUser?.company)
            contentResolver.insert(CONTENT_URI, values)
            Log.d(TAG, "Insert: $values")
            showMessage("${datafav?.name} ${R.string.favorite_added}")
        }
    }

    private fun favoriteStatus(){
        if (isFavorite){
            favorite.setImageResource(R.drawable.ic_filled_favorite)
        } else{
            favorite.setImageResource(R.drawable.ic_favorite_border)
        }
        isFavorite = true
    }

    private fun checkFavorite(favCursor: Cursor?){
        val favObject = MappingHelper.mapCursorToArrayList(favCursor)
        for(data in favObject){
            if(this.dataUser?.id == data.id){
                Log.d(TAG, "cekFav favObject: $favObject")
                Log.d(TAG, "cekData data: $data")
                isFavorite= true
            }
        }
    }
}