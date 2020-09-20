package com.kotlin.githubuser.db

import android.net.Uri
import android.provider.BaseColumns

object  DatabaseContract {
    const val AUTHORITY = "com.kotlin.githubuser"
    const val SCHEME = "content"

    internal class FavoriteColumns: BaseColumns{
        companion object{
            const val TABLE_NAME = "favorit"
            const val _ID = "_id"
            const val NAME = "name"
            const val AVATAR = "avatar"
            const val COMPANY = "company"
            const val LOCATION = "location"
            const val REPOSITORY = "repository"
            const val FOLLOWERS = "followers"
            const val FOLLOWING = "following"
            const val USERNAME = "username"

            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build()
        }
    }
}