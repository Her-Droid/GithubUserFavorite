package com.russi.consumerapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.russi.consumerapp.DetailActivity
import com.russi.consumerapp.R
import com.russi.consumerapp.adapter.ListDataFollowAdapter
import com.russi.consumerapp.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.fragment_following.*
import kotlinx.android.synthetic.main.fragment_following.view.*

class FollowingFragment : Fragment() {
    private lateinit var dataFollowing: UserViewModel
    private lateinit var listFollowAdapter: ListDataFollowAdapter

    companion object {
        fun View.showLoading() {
            visibility = View.VISIBLE
        }

        fun View.hideLoading() {
            visibility = View.GONE
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_following, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataFollowing = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
        val activity  = activity as DetailActivity
        dataFollowing.getFollowing(activity.getUsername().toString())
        dataFollowing.getFollowing.observe(viewLifecycleOwner, Observer{ following ->
            listFollowAdapter = ListDataFollowAdapter()
            view.rv_following.adapter = listFollowAdapter
            view.rv_following.layoutManager = LinearLayoutManager(context)
            listFollowAdapter.setData(following)
        })
        dataFollowing.loading.observe(viewLifecycleOwner, Observer { loading ->
            if (loading) {
                pb_following.showLoading()
            } else {
                pb_following.hideLoading()
            }
        })
        dataFollowing.message.observe(viewLifecycleOwner, Observer { message ->
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        })
    }
}