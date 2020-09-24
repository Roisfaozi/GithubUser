package com.kotlin.githubuser.activity

import android.content.ContentValues
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.kotlin.githubuser.adapter.SectionPagerAdapter
import com.kotlin.githubuser.data.User
import com.kotlin.githubuser.R
import com.kotlin.githubuser.db.DatabaseContract
import com.kotlin.githubuser.db.DatabaseContract.FavoriteColumns.Companion.CONTENT_URI
import com.kotlin.githubuser.db.DatabaseContract.FavoriteColumns.Companion.NAME
import com.kotlin.githubuser.db.DatabaseContract.FavoriteColumns.Companion.USERNAME
import com.kotlin.githubuser.db.FavoriteHelper
import com.kotlin.githubuser.helper.MappingHelper
import kotlinx.android.synthetic.main.activity_detail.*



class DetailActivity : AppCompatActivity(), View.OnClickListener {

    private var isFavorite = false
    private var dataUser: User? = null
    private lateinit var favoriteHelper: FavoriteHelper
    private lateinit var uriWithId: Uri

    companion object {
        const val EXTRA_DETAIL = "extra_detail"
        const val EXTRA_FAV = "extra_fav"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        supportActionBar?.title= "Profile"


        favoriteHelper= FavoriteHelper.getInstance(applicationContext)
        favoriteHelper.open()

        val intent = intent.getParcelableExtra(EXTRA_DETAIL) as User
        uriWithId = Uri.parse(CONTENT_URI.toString() + "/" + intent.userName)
        Log.d("iniloh", "ck: $uriWithId")
        val userFav = contentResolver?.query(uriWithId, null, null, null, null)
        checkFavorite(userFav)
        Log.d("inikak", "ck: ${checkFavorite(userFav)}")

        val cursor: Cursor = favoriteHelper.queryById(intent.userName.toString())
        if (cursor.moveToNext()){
            isFavorite = true
            favoriteStatus(isFavorite)
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
                Log.d("inisih", "cekData data: $data")
                isFavorite= true
                Log.d("inikan", "cekData fav: $isFavorite")
            }
        }
    }

    override fun onClick(v: View?) {
        val intentFav = intent.getParcelableExtra(EXTRA_DETAIL) as User
        when(v?.id) {
            R.id.favorite -> {
                if(!isFavorite){
                    val values = ContentValues()
                    values.put(DatabaseContract.FavoriteColumns.NAME, intentFav.name)
                    values.put(DatabaseContract.FavoriteColumns.AVATAR, intentFav.avatar)
                    values.put(DatabaseContract.FavoriteColumns.COMPANY, intentFav.company)
                    values.put(DatabaseContract.FavoriteColumns.LOCATION, intentFav.location)
                    values.put(DatabaseContract.FavoriteColumns.REPOSITORY, intentFav.repository)
                    values.put(DatabaseContract.FavoriteColumns.FOLLOWING, intentFav.following)
                    values.put(DatabaseContract.FavoriteColumns.FOLLOWERS, intentFav.followers)
                    values.put(DatabaseContract.FavoriteColumns.USERNAME, intentFav.userName)
                    contentResolver.insert(CONTENT_URI, values)
                    isFavorite = !isFavorite
                    favoriteStatus(isFavorite)
                    Log.d("coba", "Insert: $values")
                    showMessage("${intentFav.name} ${getString(R.string.favorite_added)}")


                } else {
                    val namFav =intentFav.userName.toString()
                    favoriteHelper.deleteById(namFav)
                    uriWithId = Uri.parse("$CONTENT_URI/$USERNAME")
                    contentResolver.delete(uriWithId, null, null)
                    Log.d("Uri", "$favoriteHelper")
                    isFavorite = !isFavorite
                    favoriteStatus(isFavorite)
                    Log.d("delete", "data terhapus")
                    showMessage("${intentFav.name} ${getString(R.string.favorite_removed)}")
                }
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val i = Intent(this@DetailActivity, MainActivity::class.java)
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        finish()
    }
}