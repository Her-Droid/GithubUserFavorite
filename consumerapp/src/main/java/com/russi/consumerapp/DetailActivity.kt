package com.russi.consumerapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.russi.consumerapp.adapter.ViewPagerDetailAdapter
import com.russi.consumerapp.model.FavoriteModel
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_NOTE = "extra_note"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        setDataFavorite()
        viewPagerConfig()

    }

    fun getUsername(): String? {
        val user = intent?.getParcelableExtra<FavoriteModel>(EXTRA_NOTE)
        return user?.login
    }

    private fun setDataFavorite() {
        val favoriteUser = intent.getParcelableExtra<FavoriteModel>(EXTRA_NOTE)
        detail_name.text = favoriteUser?.name.toString()
        username_detail.text = favoriteUser?.login.toString()
        detail_company.text = favoriteUser?.company.toString()
        detail_location.text = favoriteUser?.location.toString()
        detail_follower.text = favoriteUser?.follower.toString()
        detail_following.text = favoriteUser?.following.toString()
        Glide.with(this)
            .load(favoriteUser?.avatarUrl)
            .into(detail_image)
    }


    private fun viewPagerConfig() {
        val viewPagerDetailAdapter = ViewPagerDetailAdapter(this, supportFragmentManager)
        view_pager.adapter = viewPagerDetailAdapter
        tab_layout.setupWithViewPager(view_pager)
    }

}