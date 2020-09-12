package com.kotlin.githubuser.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.kotlin.githubuser.db.DatabaseContract.FavoriteColumns.Companion.TABLE_NAME
import com.kotlin.githubuser.db.DatabaseContract.FavoriteColumns.Companion._ID
import java.sql.SQLException

class FavoriteHelper (context: Context) {

    companion object{
        private const val DATABASE_TABLE = TABLE_NAME
        private lateinit var databaseHelper: DatabaseHelper
        private var INSTANCE: FavoriteHelper? = null
        private lateinit var database: SQLiteDatabase

        fun getInstance(context: Context): FavoriteHelper = INSTANCE ?: synchronized(this){
            INSTANCE ?: FavoriteHelper(context)
        }
    }

    init {
        databaseHelper = DatabaseHelper(context)
    }

    @Throws(SQLException::class)
    fun open(){
        database = databaseHelper.writableDatabase
    }


    fun queryAll(): Cursor{
        return database.query(
            DATABASE_TABLE,
            null,
            null,
            null,
            null,
            null,
            "$_ID ASC"
        )
    }

    fun queryById(Id: String): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            "$_ID=?",
            arrayOf(Id),
            null,
            null,
            null,
            null
        )
    }

    fun insert(values: ContentValues?): Long{
        return database.insert(DATABASE_TABLE, null, values)
    }

    fun deleteById(id:String): Int{
        return database.delete(DATABASE_TABLE, "$_ID = '$id", null)
    }

    fun update(id: String, values: ContentValues?): Int{
        open()
        return database.update(DATABASE_TABLE, values, "$_ID = ?", arrayOf(id))
    }

}