package com.kotlin.githubuser.helper

import android.database.Cursor
import android.util.Log
import com.kotlin.githubuser.data.User
import com.kotlin.githubuser.db.DatabaseContract

object MappingHelper {
    val TAG = MappingHelper::class.java.simpleName
    fun mapCursorToArrayList(cursor: Cursor?): ArrayList<User>{
        val userList = ArrayList<User>()
        cursor?.apply {
            while(moveToNext()) {
                val user = User()
                user.id = getInt(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns._ID))
                user.name = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.NAME))
                user.avatar = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.AVATAR))
                user.company = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.COMPANY))
                userList.add(user)
                Log.d(TAG, user.toString())
            }
        }
        return userList
    }
}