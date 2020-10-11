package com.russi.githubapi

import android.os.Bundle
import android.provider.ContactsContract
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.russi.githubapi.adapter.ViewPagerDetailAdapter
import com.russi.githubapi.database.FavoriteHelper
import com.russi.githubapi.model.DataUser
import com.russi.githubapi.model.DetailResponse
import com.russi.githubapi.model.FavoriteModel
import com.russi.githubapi.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    private lateinit var detailUser: UserViewModel
    private lateinit var favoriteHelper: FavoriteHelper
    private var favoriteModel: FavoriteModel? = null

    companion object {
        const val EXTRA_DATA = "extra_data"
        const val EXTRA_FAVORITE = "extra_data"
        const val EXTRA_NOTE = "extra_note"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        favoriteHelper = FavoriteHelper.getInstance(applicationContext)
        favoriteHelper.open()

        favoriteModel = intent.getParcelableExtra(EXTRA_NOTE)
        if (favoriteModel != null){
            setDataFavorite()
            val checked: Int = R.drawable.ic_baseline_favorite_24
            btn_favorite.setImageResource(checked)
        } else{
            setData()
        }

        detailUser = ViewModelProvider(this).get(UserViewModel::class.java)
        viewPagerConfig()

        if (intent.hasExtra(EXTRA_DATA)) {
            val user = intent.getParcelableExtra<DataUser>(EXTRA_DATA)
            if (user?.username?.isNotEmpty()!!) {
                title = user.username
                detailUser.getDetailUser(user.username)
            } else {
                title = user.login
                detailUser.getDetailUser(user.login)
            }
        }
        val dataUser = intent.getParcelableExtra<DataUser>(EXTRA_DATA)
        detailUser.dataDetailUser.observe(this, Observer { response ->
            if(!response.location.isNullOrEmpty()) {
                detail_location.text = response.location.toString()
            }else{
                detail_location.text = "Lokasi tidak Ada"
            }

            if(!response.company.isNullOrEmpty()) {
                detail_company.text = response.company.toString()
            }else{
                detail_company.text = "Company tidak Ada"
            }

            if(!response.login.isNullOrEmpty()) {
                username_detail.text = response.login.toString()
            }else{
                username_detail.text = "Username tidak Ada"
            }

            if(!response.name.isNullOrEmpty()) {
                detail_name.text = response.name.toString()
            }else{
                detail_name.text = "Nama tidak Ada"
            }

            detail_follower.text = response.followers.toString()
            detail_following.text = response.following.toString()
            Glide.with(this).load(response.avatarUrl)
                .into(detail_image)
        })
        detailUser.message.observe(this, Observer{message ->
            Toast.makeText(this,message, Toast.LENGTH_SHORT).show()
        })
    }


    private fun setData() {
        val dataFavoriteModel = intent.getParcelableExtra<FavoriteModel>(EXTRA_DATA)
        detail_name.text = dataFavoriteModel?.name.toString()
        username_detail.text = dataFavoriteModel?.username.toString()
        detail_company.text = dataFavoriteModel?.company.toString()
        detail_location.text = dataFavoriteModel?.location.toString()
        detail_follower.text = dataFavoriteModel?.follower.toString()
        detail_following.text = dataFavoriteModel?.following.toString()
        Glide.with(this)
            .load(dataFavoriteModel?.avatar.toString())
            .into(detail_image)
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
            .load(favoriteUser?.avatar.toString())
            .into(detail_image)
    }

    fun getUsername(): String? {
        val user = intent?.getParcelableExtra<DataUser>(EXTRA_DATA)
        return if(user?.username?.isNotEmpty()!!){
            user.username
        }else{
            user.login
        }
    }

    private fun viewPagerConfig() {
        val viewPagerDetailAdapter = ViewPagerDetailAdapter(this, supportFragmentManager)
        view_pager.adapter = viewPagerDetailAdapter
        tab_layout.setupWithViewPager(view_pager)
    }

}