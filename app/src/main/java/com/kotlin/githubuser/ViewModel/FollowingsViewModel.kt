package com.kotlin.githubuser.ViewModel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kotlin.githubuser.BuildConfig
import com.kotlin.githubuser.Fragment.FollowingsFragment
import com.kotlin.githubuser.Data.Follow
import com.kotlin.githubuser.Data.User
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject

class FollowingsViewModel : ViewModel(){
    private val listFollowing = ArrayList<User>()

    val mutableFollowing = MutableLiveData<ArrayList<User>>()
    companion object {
        private val TAG = FollowingsViewModel::class.java.simpleName
        private const val TOKEN = "token ${BuildConfig.API_KEY}"
    }

    fun getFollowing(context: Context, userName: String): LiveData<ArrayList<User>> {

        val url = "https://api.github.com/users/$userName/following"

        val client = AsyncHttpClient()
        client.addHeader("Authorization", TOKEN)
        client.addHeader("User-agent", "request")
        client.get(url, object: AsyncHttpResponseHandler(){
            override fun onSuccess(statusCode: Int, headers: Array<Header>?, responseBody: ByteArray) {
                val result = String(responseBody)
                try {
                    val jsonArray = JSONArray(result)
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val userName = jsonObject.getString("login")
                        getUserDetail(userName, context)
                    }
                    Log.d(TAG, statusCode.toString())

                } catch (e:Exception){
                    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }

            override fun onFailure( statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
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

        return  mutableFollowing
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
                    listFollowing.add(user)
                    mutableFollowing.postValue(listFollowing)
                    Log.d("hayo", listFollowing.toString())
                } catch (e:Exception){
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
                Log.d(TAG, "Ra Keno")
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }
}