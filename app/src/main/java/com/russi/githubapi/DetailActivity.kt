package com.russi.githubapi

import android.content.ContentValues
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.russi.githubapi.adapter.ViewPagerDetailAdapter
import com.russi.githubapi.database.FavoriteHelper
import com.russi.githubapi.database.GithubDatabase
import com.russi.githubapi.database.GithubDatabase.FavoriteTable.Companion.USER_CONTENT_URI
import com.russi.githubapi.model.DataUser
import com.russi.githubapi.model.DetailResponse
import com.russi.githubapi.model.FavoriteModel
import com.russi.githubapi.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var detailUser: UserViewModel
    private lateinit var favoriteHelper: FavoriteHelper
    private var favorite = false
    private lateinit var detailResponse: DetailResponse
    private lateinit var username : String
    companion object {
        const val EXTRA_DATA = "extra_data"
        const val EXTRA_DATA_FAVORITE = "extra_data_favorite"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        detailUser = ViewModelProvider(this).get(UserViewModel::class.java)

        if(intent.hasExtra(EXTRA_DATA)){
            val data = intent.getParcelableExtra<DataUser>(EXTRA_DATA)!!
            title = data.login
            username = data.login
        }else{
            val data = intent.getParcelableExtra<FavoriteModel>(EXTRA_DATA_FAVORITE)!!
            title = data.login
            username = data.login
        }

        detailUser.getDetailUser(username)

        favoriteHelper = FavoriteHelper.getInstance(applicationContext)
        favoriteHelper.open()


        if(favoriteHelper.queryByIdUserFavorite(username) != null){
            if(favoriteHelper.queryByIdUserFavorite(username).moveToFirst()){
                favorite = true
            }
        }

        setButtonFavorite()

        viewPagerConfig()
        btn_favorite.setOnClickListener(this)


        detailUser.dataDetailUser.observe(this, Observer { response ->
            detailResponse = response
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


    private fun setButtonFavorite(){
        if(favorite){
            btn_favorite.setImageResource(R.drawable.ic_baseline_favorite_24)
        }else{
            btn_favorite.setImageResource(R.drawable.ic_baseline_favorite_border_24)
        }
    }

    fun getUsername(): String? {
        return username
    }

    private fun viewPagerConfig() {
        val viewPagerDetailAdapter = ViewPagerDetailAdapter(this, supportFragmentManager)
        view_pager.adapter = viewPagerDetailAdapter
        tab_layout.setupWithViewPager(view_pager)
    }

    override fun onDestroy() {
        super.onDestroy()
        favoriteHelper.close()
    }

    override fun onClick(view: View?) {
        if (view?.id == R.id.btn_favorite) {
            if (favorite) {
                favoriteHelper.deleteByIdUserFavorite(detailResponse.login)
                Toast.makeText(this, getString(R.string.onclik), Toast.LENGTH_SHORT).show()
                favorite = false
                setButtonFavorite()
            } else {
                val dataUsername = username_detail.text.toString()
                val dataName = detail_name.text.toString().trim()
                val dataAvatar = detailResponse.avatarUrl
                val dataCompany = detail_company.text.toString().trim()
                val dataLocation = detail_location.text.toString().trim()
                val dataFollowers = detail_follower.text.toString().trim()
                val dataFollowing = detail_following.text.toString().trim()
                val dataFavorite = "1"

                val values = ContentValues()
                values.put(GithubDatabase.FavoriteTable.USERNAME, dataUsername)
                values.put(GithubDatabase.FavoriteTable.NAME, dataName)
                values.put(GithubDatabase.FavoriteTable.AVATAR, dataAvatar)
                values.put(GithubDatabase.FavoriteTable.COMPANY, dataCompany)
                values.put(GithubDatabase.FavoriteTable.LOCATION, dataLocation)
                values.put(GithubDatabase.FavoriteTable.FOLLOWERS, dataFollowers)
                values.put(GithubDatabase.FavoriteTable.FOLLOWING, dataFollowing)
                values.put(GithubDatabase.FavoriteTable.FAVORITE, dataFavorite)

                favorite = true
                contentResolver.insert(USER_CONTENT_URI, values)
                Toast.makeText(this, getString(R.string.favorite_toast_detail), Toast.LENGTH_SHORT).show()
                setButtonFavorite()
            }
        }
    }
}