package com.kotlin.githubuser.fragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.kotlin.githubuser.R
import com.kotlin.githubuser.adapter.FollowingAdapter
import com.kotlin.githubuser.data.User
import com.kotlin.githubuser.viewModel.FollowingsViewModel
import kotlinx.android.synthetic.main.fragment_followings.*


class FollowingsFragment : Fragment() {

    companion object{
        const val EXTRA_DETAIL = "extra_detail"
    }

    val listFollow: ArrayList<User> = ArrayList()
    private lateinit var adapter : FollowingAdapter
    private lateinit var followingView : FollowingsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_followings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        followingView = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(FollowingsViewModel::class.java)
        adapter= FollowingAdapter(listFollow)
        rv_followings.adapter =adapter

        val user = activity?.intent?.getParcelableExtra(EXTRA_DETAIL) as? User


        followingView.getFollowing(activity!!.applicationContext, user?.userName.toString()).observe(activity!!, Observer { listFollowing->
            if (listFollowing != null){
                adapter.setFollow(listFollowing)
                showLoading(false)
            }
        })
        showRecycler()

    }


    private fun showRecycler(){
        val layourManager = LinearLayoutManager(activity)
        rv_followings.setLayoutManager(layourManager)
        rv_followings.setHasFixedSize(true)
    }


    private fun showLoading(state : Boolean){
        if (state) {
            progres_followings.visibility = View.VISIBLE
        } else{
            progres_followings.visibility = View.INVISIBLE
        }
    }


}