package com.kotlin.githubuser.helper

import android.database.Cursor
import android.util.Log
import com.kotlin.githubuser.data.User
import com.kotlin.githubuser.db.DatabaseContract

object MappingHelper {
    fun mapCursorToArrayList(cursor: Cursor?): ArrayList<User>{
        val userList = ArrayList<User>()
        cursor?.apply {
            while(moveToNext()) {
                val user = User()
                user.name = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.NAME))
                user.avatar = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.AVATAR))
                user.company = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.COMPANY))
                user.repository = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.REPOSITORY))
                user.followers = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.FOLLOWERS))
                user.following = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.FOLLOWING))
                user.location = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.LOCATION))
                user.userName = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.USERNAME))
                userList.add(user)
                Log.d("kirim: MappingHelper", user.toString())
            }
        }
        return userList
    }
}