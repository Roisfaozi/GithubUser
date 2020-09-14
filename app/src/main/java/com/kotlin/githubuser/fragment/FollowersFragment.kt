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
import com.kotlin.githubuser.adapter.FollowerAdapter
import com.kotlin.githubuser.data.User
import com.kotlin.githubuser.viewModel.FollowersViewModel
import kotlinx.android.synthetic.main.fragment_followers.*


class FollowersFragment : Fragment() {

    companion object {
        const val EXTRA_DETAIL = "extra_detail"
    }

    val lisFollow : ArrayList<User> = ArrayList()
    private lateinit var adapter : FollowerAdapter
    private lateinit var followerView : FollowersViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_followers, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        followerView= ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(FollowersViewModel::class.java)
        adapter = FollowerAdapter(lisFollow)
        rv_followers.adapter = adapter

        val user = activity?.intent?.getParcelableExtra(EXTRA_DETAIL) as? User

        followerView.getFollowers(activity!!.applicationContext, user?.userName.toString()).observe(activity!!, Observer { listFollower->
            if (listFollower != null){
                adapter.setFollower(listFollower)
                showLoading(false)
            }
        })

        showRecycler()


    }

    private fun showRecycler(){
        val layoutManager = LinearLayoutManager(activity)
        rv_followers.setLayoutManager(layoutManager)
        rv_followers.setHasFixedSize(true)
    }

    private fun showLoading(state: Boolean){
        if (state) {
            progres_followers.visibility = View.VISIBLE
        } else {
            progres_followers.visibility = View.INVISIBLE
        }
    }




}