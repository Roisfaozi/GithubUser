package com.kotlin.githubuser.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.kotlin.githubuser.db.DatabaseContract.AUTHORITY
import com.kotlin.githubuser.db.DatabaseContract.FavoriteColumns.Companion.CONTENT_URI
import com.kotlin.githubuser.db.DatabaseContract.FavoriteColumns.Companion.TABLE_NAME
import com.kotlin.githubuser.db.FavoriteHelper

class MyContentProvider : ContentProvider() {

    companion object{
        private const val FAV = 1
        private const val FAV_ID = 2
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        private lateinit var favoriteHelper: FavoriteHelper

        init{

            sUriMatcher.addURI(AUTHORITY, TABLE_NAME, FAV)
            sUriMatcher.addURI(AUTHORITY, "$TABLE_NAME/*", FAV_ID)
        }
    }

    override fun onCreate(): Boolean {
        favoriteHelper = FavoriteHelper.getInstance(context as Context)
        favoriteHelper.open()
        return true
    }
    override fun query(uri: Uri, projection: Array<String>?, selection: String?, selectionArgs: Array<String>?, sortOrder: String?): Cursor? {

        val cursor: Cursor?
        when (sUriMatcher.match(uri)) {
            FAV -> cursor = favoriteHelper.queryAll()
            FAV_ID-> cursor = favoriteHelper.queryById(uri.lastPathSegment.toString())
            else -> cursor = null
        }
        return cursor
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val added: Long = when(FAV){
            sUriMatcher.match(uri) -> favoriteHelper.insert(values)
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return Uri.parse("$CONTENT_URI/$added")
    }
    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
        val update: Int = when (FAV_ID){
            sUriMatcher.match(uri) -> favoriteHelper.update(uri.lastPathSegment.toString(), values)
            else ->0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return update
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val delete: Int = when(FAV_ID){
            sUriMatcher.match(uri) -> favoriteHelper.deleteById(uri.lastPathSegment.toString())
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return delete
    }
}
