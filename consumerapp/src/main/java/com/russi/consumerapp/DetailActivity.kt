package com.russi.consumerapp

import android.nfc.NfcAdapter.EXTRA_DATA
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.russi.consumerapp.adapter.ViewPagerDetailAdapter
import com.russi.consumerapp.model.DataUser
import com.russi.consumerapp.model.DetailResponse
import com.russi.consumerapp.model.FavoriteModel
import com.russi.consumerapp.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {
    private lateinit var detailUser: UserViewModel

    companion object {
        const val EXTRA_POSITION = "extra_position"
        const val EXTRA_NOTE = "extra_note"
        const val EXTRA_DATA_FAVORITE = "extra_data_favorite"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        setDataFavorite()
        viewPagerConfig()

    }

    fun getUsername(): String? {
        val user = intent?.getParcelableExtra<FavoriteModel>(EXTRA_NOTE)
        return if(user?.username?.isNotEmpty()!!){
            user.username
        }else{
            user.login
        }
    }

    private fun setDataFavorite() {
        val favoriteUser = intent.getParcelableExtra<FavoriteModel>(EXTRA_NOTE)
        detail_name.text = favoriteUser?.name.toString()
        username_detail.text = favoriteUser?.username.toString()
        detail_company.text = favoriteUser?.company.toString()
        detail_location.text = favoriteUser?.location.toString()
        detail_follower.text = favoriteUser?.follower.toString()
        detail_following.text = favoriteUser?.following.toString()
        Glide.with(this)
            .load(favoriteUser?.avatarUrl.toString())
            .into(detail_image)
    }


    private fun viewPagerConfig() {
        val viewPagerDetailAdapter = ViewPagerDetailAdapter(this, supportFragmentManager)
        view_pager.adapter = viewPagerDetailAdapter
        tab_layout.setupWithViewPager(view_pager)
    }

}