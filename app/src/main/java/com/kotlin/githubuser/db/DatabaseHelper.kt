package com.kotlin.githubuser.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.kotlin.githubuser.db.DatabaseContract.FavoriteColumns.Companion.AVATAR
import com.kotlin.githubuser.db.DatabaseContract.FavoriteColumns.Companion.COMPANY
import com.kotlin.githubuser.db.DatabaseContract.FavoriteColumns.Companion.FOLLOWERS
import com.kotlin.githubuser.db.DatabaseContract.FavoriteColumns.Companion.FOLLOWING
import com.kotlin.githubuser.db.DatabaseContract.FavoriteColumns.Companion.LOCATION
import com.kotlin.githubuser.db.DatabaseContract.FavoriteColumns.Companion.NAME
import com.kotlin.githubuser.db.DatabaseContract.FavoriteColumns.Companion.REPOSITORY
import com.kotlin.githubuser.db.DatabaseContract.FavoriteColumns.Companion.TABLE_NAME
import com.kotlin.githubuser.db.DatabaseContract.FavoriteColumns.Companion.USERNAME
import com.kotlin.githubuser.db.DatabaseContract.FavoriteColumns.Companion._ID

internal class DatabaseHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object{
        private const val DATABASE_NAME = "data_githubuser"
        private const val DATABASE_VERSION = 1

        private val SQL_CREATE_TABLE_FAV = "CREATE TABLE $TABLE_NAME" +
                "($_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                " $NAME TEXT NOT NULL UNIQUE," +
                " $AVATAR TEXT NOT NULL," +
                " $COMPANY TEXT NOT NULL," +
                " $LOCATION TEXT NOT NULL," +
                " $REPOSITORY TEXT NOT NULL," +
                " $FOLLOWERS TEXT NOT NULL," +
                " $USERNAME TEXT NOT NULL," +
                " $FOLLOWING TEXT NOT NULL)"

        private const val SQL_DROP_TABLE_FAV = "DROP TABLE IF EXISTS $TABLE_NAME"
    }

    override fun onCreate(db: SQLiteDatabase?) {
       db?.execSQL(SQL_CREATE_TABLE_FAV)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
       db?.execSQL(SQL_DROP_TABLE_FAV)
        onCreate(db)
    }
}