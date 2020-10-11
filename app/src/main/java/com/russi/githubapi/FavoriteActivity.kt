package com.russi.githubapi

import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.russi.githubapi.adapter.ListFavoriteAdapter
import com.russi.githubapi.database.GithubDatabase.FavoriteTable.Companion.USER_CONTENT_URI
import com.russi.githubapi.helper.MapHelper
import com.russi.githubapi.model.FavoriteModel
import kotlinx.android.synthetic.main.activity_favorite.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity() {
    private lateinit var listFavoriteAdapter: ListFavoriteAdapter
    companion object{
        private const val EXTRA_DATA = "EXTRA_DATA"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        title = getString(R.string.favorite)

        rv_favorite.layoutManager = LinearLayoutManager(this)
        rv_favorite.setHasFixedSize(true)
        listFavoriteAdapter = ListFavoriteAdapter()
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
            pb_favorite.visibility = View.VISIBLE
            val noteAsync  = async(Dispatchers.IO){
                val cursor = contentResolver.query(USER_CONTENT_URI, null,null,null,null)
                MapHelper.mapCursorToArrayList(cursor)
            }
            val dataFavorite = noteAsync.await()
            pb_favorite.visibility = View.INVISIBLE
            if (dataFavorite.size > 0 ){
                listFavoriteAdapter.listFavorite =  dataFavorite
            }else{
                listFavoriteAdapter.listFavorite = arrayListOf()
                showMessage()
            }
        }
    }
    private fun showMessage() {
        Snackbar.make(rv_favorite, getString(R.string.empty_fav), Snackbar.LENGTH_SHORT).show()
    }


    override fun onResume() {
        super.onResume()
        showSelectedUser()
    }

}