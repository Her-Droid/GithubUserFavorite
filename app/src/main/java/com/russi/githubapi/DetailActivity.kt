package com.russi.githubapi

import android.content.ContentValues
import android.os.Bundle
import android.provider.ContactsContract
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.russi.githubapi.adapter.ViewPagerDetailAdapter
import com.russi.githubapi.database.FavoriteHelper
import com.russi.githubapi.database.GithubDatabase.FavoriteTable.Companion.AVATAR
import com.russi.githubapi.database.GithubDatabase.FavoriteTable.Companion.COMPANY
import com.russi.githubapi.database.GithubDatabase.FavoriteTable.Companion.FAVORITE
import com.russi.githubapi.database.GithubDatabase.FavoriteTable.Companion.FOLLOWERS
import com.russi.githubapi.database.GithubDatabase.FavoriteTable.Companion.FOLLOWING
import com.russi.githubapi.database.GithubDatabase.FavoriteTable.Companion.LOCATION
import com.russi.githubapi.database.GithubDatabase.FavoriteTable.Companion.NAME
import com.russi.githubapi.database.GithubDatabase.FavoriteTable.Companion.USERNAME
import com.russi.githubapi.database.GithubDatabase.FavoriteTable.Companion.USER_CONTENT_URI
import com.russi.githubapi.model.DataUser
import com.russi.githubapi.model.DetailResponse
import com.russi.githubapi.model.FavoriteModel
import com.russi.githubapi.viewmodel.UserViewModel
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.activity_detail.view.*
import kotlinx.android.synthetic.main.list_user.view.*

class DetailActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var detailUser: UserViewModel
    private lateinit var favoriteHelper: FavoriteHelper
    private var favoriteModel: FavoriteModel? = null
    private var favorite = false
    private lateinit var imageAvatar: String
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
        }else{
            setData()
        }
        viewPagerConfig()
        btn_favorite.setOnClickListener(this)
        detailUser = ViewModelProvider(this).get(UserViewModel::class.java)
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
        val dataFavoriteModel = intent.getParcelableExtra<DataUser>(EXTRA_DATA)
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
        supportActionBar?.elevation = 0f
    }

    override fun onDestroy() {
        super.onDestroy()
        favoriteHelper.close()
    }

    override fun onClick(view: View?) {
        val favoriteUser = intent.getParcelableExtra<FavoriteModel>(EXTRA_NOTE)
        val checked: Int = R.drawable.ic_baseline_favorite_24
        val unChecked: Int = R.drawable.ic_baseline_favorite_border_24
        if (view?.id == R.id.btn_favorite) {
            if (favorite) {
                favoriteHelper.deleteByIdUserFavorite(favoriteModel?.username.toString())
                Toast.makeText(this, getString(R.string.onclik), Toast.LENGTH_SHORT).show()
                btn_favorite.setImageResource(unChecked)
                favorite = false
            } else {
                val dataUsername = username_detail.text.toString()
                val dataName = detail_name.text.toString()
                val imgUser: String = view.detail_image.toString()
                val dataCompany = detail_company.text.toString()
                val dataLocation = detail_location.text.toString()
                val dataFollowers = detail_follower.text.toString()
                val dataFollowing = detail_following.text.toString()
                val dataFavorite = "1"

                val values = ContentValues()
                values.put(USERNAME, dataUsername)
                values.put(NAME, dataName)
                values.put(AVATAR, imgUser)
                values.put(COMPANY, dataCompany)
                values.put(LOCATION, dataLocation)
                values.put(FOLLOWERS, dataFollowers)
                values.put(FOLLOWING, dataFollowing)
                values.put(FAVORITE, dataFavorite)

                favorite = true
                contentResolver.insert(USER_CONTENT_URI, values)
                Toast.makeText(this, getString(R.string.favorite_toast_detail), Toast.LENGTH_SHORT).show()
                btn_favorite.setImageResource(checked)
            }
        }
    }
    private fun setActionBarTitle(title: String) {
        if (supportActionBar != null) {
            supportActionBar!!.title = title
        }
    }

}