package com.kotlin.githubuser.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.kotlin.githubuser.db.DatabaseContract.FavoriteColumns.Companion.TABLE_NAME
import com.kotlin.githubuser.db.DatabaseContract.FavoriteColumns.Companion.NAME
import com.kotlin.githubuser.db.DatabaseContract.FavoriteColumns.Companion.USERNAME
import com.kotlin.githubuser.db.DatabaseContract.FavoriteColumns.Companion._ID
import java.sql.SQLException

class FavoriteHelper (context: Context) {

    companion object{
        private const val DATABASE_TABLE = TABLE_NAME
        private lateinit var databaseHelper: DatabaseHelper
        private lateinit var database: SQLiteDatabase
        private var INSTANCE: FavoriteHelper? = null

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

    fun close() {
        databaseHelper.close()
        if (database.isOpen)
            database.close()
    }


    fun queryAll(): Cursor{
        return database.query(
            DATABASE_TABLE,
            null,
            null,
            null,
            null,
            null,
            "$NAME ASC"
        )
    }

    fun queryById(id: String): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            "$USERNAME=?",
            arrayOf(id),
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
        return database.delete(DATABASE_TABLE, "$USERNAME = '$id'", null)
    }

    fun update(id: String, values: ContentValues?): Int{
        open()
        return database.update(DATABASE_TABLE, values, "$NAME = ?", arrayOf(id))
    }

}