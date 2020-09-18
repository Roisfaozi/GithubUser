package com.kotlin.githubuser.activity

import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.kotlin.githubuser.adapter.SectionPagerAdapter
import com.kotlin.githubuser.data.User
import com.kotlin.githubuser.R
import com.kotlin.githubuser.db.DatabaseContract
import com.kotlin.githubuser.db.DatabaseContract.FavoriteColumns.Companion.CONTENT_URI
import com.kotlin.githubuser.db.FavoriteHelper
import com.kotlin.githubuser.helper.MappingHelper
import kotlinx.android.synthetic.main.activity_detail.*



class DetailActivity : AppCompatActivity(), View.OnClickListener {

    private var isFavorite = false
    private var dataUser: User? = null
    private lateinit var favoriteHelper: FavoriteHelper
    private lateinit var uriWithId: Uri

    companion object {
        internal val TAG = DetailActivity::class.java.simpleName
        const val EXTRA_DETAIL = "extra_detail"
        const val EXTRA_FAV = "extra_fav"
        const val EXTRA_STATE = "extra_state"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)


        favoriteHelper= FavoriteHelper.getInstance(applicationContext)
        favoriteHelper.open()

        uriWithId = Uri.parse(CONTENT_URI.toString() + "/" + dataUser?.id)
        val userFav = contentResolver?.query(uriWithId, null, null, null, null)
        checkFavorite(userFav)

        val intent = intent.getParcelableExtra(EXTRA_DETAIL) as User
        val cursor: Cursor = favoriteHelper.queryById(intent.id.toString())
        if (cursor.moveToNext()){
            isFavorite = true
            favoriteStatus(true)
        }

        setUser()
        favorite.setOnClickListener(this)
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


    private fun favoriteStatus(isFavorite : Boolean){
        if (isFavorite){
            favorite.setImageResource(R.drawable.ic_filled_favorite)
        } else{
            favorite.setImageResource(R.drawable.ic_favorite_border)
        }
    }

    private fun checkFavorite(favCursor: Cursor?){
        val favObject = MappingHelper.mapCursorToArrayList(favCursor)
        for(data in favObject){
            if(this.dataUser?.id == data.id){
                Log.d("ini", "cekFav favObject: $favObject")
                Log.d("ini", "cekData data: $data")
                isFavorite= true
            }
        }
    }

    override fun onClick(v: View?) {
        val intentFav = intent.getParcelableExtra(EXTRA_DETAIL) as User
        when(v?.id) {
            R.id.favorite -> {
                if(isFavorite){
                    val favId = intentFav.userName.toString()
                    favoriteHelper.deleteById(favId)
                    Log.d(TAG, R.string.favorite_removed.toString())
                    showMessage("${dataUser?.name} ${R.string.favorite_added}")
                    favoriteStatus(false)
                    isFavorite = true
                } else {
                    val values = ContentValues()
                    values.put(DatabaseContract.FavoriteColumns.NAME, intentFav.name)
                    values.put(DatabaseContract.FavoriteColumns.AVATAR, intentFav.avatar)
                    values.put(DatabaseContract.FavoriteColumns.COMPANY, intentFav.company)

                    isFavorite = false
                    contentResolver.insert(CONTENT_URI, values)
                    Log.d("coba", "Insert: $values")
                    showMessage("${dataUser?.name} ${R.string.favorite_added}")
                    favoriteStatus(true)
                }
            }
        }
    }
}