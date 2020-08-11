package com.kotlin.githubuser.ViewModel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kotlin.githubuser.Activity.MainActivity
import com.kotlin.githubuser.BuildConfig
import com.kotlin.githubuser.Data.User
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject

class MainViewModel : ViewModel() {
    private val listUser = ArrayList<User>()
    private val mutableListUser = MutableLiveData<ArrayList<User>>()

    companion object {
        private val EXTRA = MainViewModel::class.java.simpleName
        private const val TOKEN = "token ${BuildConfig.API_KEY}"
    }

    fun getListUsers() : LiveData<ArrayList<User>>{
        return mutableListUser
    }

    fun getDataUser(context: Context){
        val url = "https://api.github.com/users"

        val client = AsyncHttpClient()
        client.addHeader("Authorization", TOKEN)
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>, responseBody: ByteArray) {
                val result = String(responseBody)

                try {
                    val jsonArray = JSONArray(result)
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val login = jsonObject.getString("login")
                        getUserDetail(login, context)
                    }
                } catch (e:Exception) {
                    Log.d(EXTRA, "onSuccess: ${e.message}")
                    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>,
                responseBody: ByteArray,
                error: Throwable
            ) {
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Log.d(EXTRA, errorMessage)
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })

    }

    fun getUserDetail(login: String, context: Context) {
        val url = "https://api.github.com/users/$login"

        val client = AsyncHttpClient()
        client.addHeader("Authorization", TOKEN)
        client.addHeader("User-Agent", "request")

        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>, responseBody: ByteArray) {
                val result = String(responseBody)

                try {
                    val jsonObject = JSONObject(result)
                    val user = User()
                    user.name = jsonObject.getString("name")
                    user.userName = jsonObject.getString("login")
                    user.location = jsonObject.getString("location")
                    user.repository = jsonObject.getString("public_repos")
                    user.company = jsonObject.getString("company")
                    user.following = jsonObject.getString("following")
                    user.followers = jsonObject.getString("followers")
                    user.avatar = jsonObject.getString("avatar_url")
                    listUser.add(user)
                    mutableListUser.postValue(listUser)
                } catch (e:Exception){
                    Log.d(EXTRA, "onSuccess: ${e.message}")
                    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>,
                responseBody: ByteArray,
                error: Throwable
            ) {
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Log.d(EXTRA, "onFailure: ${errorMessage}")
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun getUserSearch(query: String, context: Context) {
        val url = "https://api.github.com/search/users?q=$query"

        val client = AsyncHttpClient()
        client.addHeader("Authorization", BuildConfig.API_KEY)
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>, responseBody: ByteArray) {

                val result = String(responseBody)
                try {
                    listUser.clear()
                    val jsonArray = JSONObject(result)
                    val items = jsonArray.getJSONArray("items")
                    for (i in 0 until items.length()) {
                        val jsonObject = items.getJSONObject(i)
                        val username = jsonObject.getString("login")
                        getUserDetail(username, context)
                    }
                } catch (e:Exception) {
                    Log.d(EXTRA, "Sukses: ${e.message}")
                    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>,
                responseBody: ByteArray,
                error: Throwable
            ) {
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Log.d(EXTRA, "Gagal: ${errorMessage}")
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })

    }
}