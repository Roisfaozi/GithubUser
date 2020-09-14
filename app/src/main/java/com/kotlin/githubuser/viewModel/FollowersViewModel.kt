package com.kotlin.githubuser.viewModel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kotlin.githubuser.BuildConfig
import com.kotlin.githubuser.data.User
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject

class FollowersViewModel : ViewModel(){
    private val listFollowers = ArrayList<User>()

    val mutableFollowers = MutableLiveData<ArrayList<User>>()
    companion object {
        private val TAG = FollowersViewModel::class.java.simpleName
        private const val TOKEN = "token ${BuildConfig.API_KEY}"
    }

    fun getFollowers(context: Context, userName: String): LiveData<ArrayList<User>>{
        val url = "https://api.github.com/users/$userName/followers"

        val client = AsyncHttpClient()
        client.addHeader("Authorization", TOKEN)
        client.addHeader("User-agent", "request")
        client.get(url, object : AsyncHttpResponseHandler(){
            override fun onSuccess(succsessCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                val result = String(responseBody)

                try {
                    val jsonArray = JSONArray(result)

                    for (i in 0 until jsonArray.length()){
                        val jsonObject = jsonArray.getJSONObject(i)
                        val userLogin = jsonObject.getString("login")
                        getUserDetail(userLogin, context)

                    }

                    }catch (e:Exception){
                    Log.d(TAG, "Berhasill")
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
                Log.d(TAG, "Gagal")
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
        return mutableFollowers
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
                    listFollowers.add(user)
                    mutableFollowers.postValue(listFollowers)

                } catch (e:Exception){
                    Log.d("cari", e.toString())
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
                Log.d("ceklis", statusCode.toString())
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }
}