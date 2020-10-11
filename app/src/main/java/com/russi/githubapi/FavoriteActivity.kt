package com.russi.githubapi

import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.russi.githubapi.adapter.ListFavoriteAdapter
import com.russi.githubapi.database.GithubDatabase.FavoriteTable.Companion.USER_CONTENT_URI
import com.russi.githubapi.helper.MapHelper
import com.russi.githubapi.model.FavoriteModel
import com.russi.githubapi.viewmodel.FavoriteViewModel
import kotlinx.android.synthetic.main.activity_favorite.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity() {
    private lateinit var listFavoriteAdapter: ListFavoriteAdapter
    private lateinit var favoriteViewModel: FavoriteViewModel
    companion object{
        private const val EXTRA_DATA = "EXTRA_DATA"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)
        setActionBar()
        showLoading()

        rv_favorite.layoutManager = LinearLayoutManager(this)
        rv_favorite.setHasFixedSize(true)
        listFavoriteAdapter = ListFavoriteAdapter(this)
        rv_favorite.adapter = listFavoriteAdapter

        val thread = HandlerThread("Observer")
        thread.start()
        val handler = Handler(thread.looper)
        val observer = object : ContentObserver(handler){
            override fun onChange(selfChange: Boolean) {
                showSelectedUser()
            }
        }
        contentResolver.registerContentObserver(USER_CONTENT_URI, true, observer)
        if (savedInstanceState == null){
            showSelectedUser()
        }else{
            val listFavoriteUser = savedInstanceState.getParcelableArrayList<FavoriteModel>(EXTRA_DATA)
            if (listFavoriteUser != null){
                listFavoriteAdapter.listFavorite = listFavoriteUser
            }
        }
    }

    private fun showSelectedUser() {
        GlobalScope.launch(Dispatchers.Main) {
            val noteAsync  = async(Dispatchers.IO){
                val cursor = contentResolver.query(USER_CONTENT_URI, null,null,null,null)
                MapHelper.mapCursorToArrayList(cursor)
            }
            val dataFavorite = noteAsync.await()
            if (dataFavorite.size > 0 ){
                listFavoriteAdapter.listFavorite =  dataFavorite
            }else{
                listFavoriteAdapter.listFavorite = arrayListOf()
                showMessage()
            }
        }
    }
    private fun showLoading() {
        favoriteViewModel.loading.observe(this, { state ->
            if (state) {
                pb_favorite.visibility = View.VISIBLE
            } else {
                pb_favorite.visibility = View.INVISIBLE
            }
        })
        favoriteViewModel.message.observe(this, { message ->
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        })
    }

    private fun showMessage() {
        Snackbar.make(rv_favorite, getString(R.string.empty_fav), Snackbar.LENGTH_SHORT).show()
    }

    private fun setActionBar() {
        setSupportActionBar(toolbar_favorite)
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar_favorite.title = getString(R.string.favorite)
    }

    override fun onResume() {
        super.onResume()
        showSelectedUser()
    }

}