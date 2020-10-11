package com.russi.consumerapp

import android.content.Intent
import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.russi.consumerapp.adapter.ListFavoriteAdapter
import com.russi.consumerapp.database.GithubDatabase.FavoriteTable.Companion.USER_CONTENT_URI
import com.russi.consumerapp.helper.MapHelper
import com.russi.consumerapp.model.FavoriteModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var listDataUserAdapter: ListFavoriteAdapter

    companion object {
        private const val EXTRA_DATA = "EXTRA_DATA"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setActionBarTitle("Favorite")
        rv_user.layoutManager = LinearLayoutManager(this)
        rv_user.setHasFixedSize(true)
        listDataUserAdapter = ListFavoriteAdapter(this)
        rv_user.adapter = listDataUserAdapter

        val thread = HandlerThread("Observer")
        thread.start()
        val handler = Handler(thread.looper)
        val observer = object : ContentObserver(handler) {
            override fun onChange(selfChange: Boolean) {
                showSelectedUser()
            }
        }
        contentResolver.registerContentObserver(USER_CONTENT_URI, true, observer)

        if (savedInstanceState == null) {
            showSelectedUser()
        } else {
            val list = savedInstanceState.getParcelableArrayList<FavoriteModel>(EXTRA_DATA)
            if (list != null) {
                listDataUserAdapter.listFavorite = list
            }
        }
    }

    private fun setActionBarTitle(title: String) {
        if (supportActionBar != null) {
            supportActionBar!!.title = title
        }
    }

    private fun showSelectedUser() {
        GlobalScope.launch(Dispatchers.Main) {
            progressBar.visibility = View.VISIBLE
            val notes = async(Dispatchers.IO) {
                val cursor = contentResolver?.query(USER_CONTENT_URI, null, null, null, null)
                MapHelper.mapCursorToArrayList(cursor)
            }
            val dataFavorite = notes.await()
            progressBar.visibility = View.INVISIBLE
            if (dataFavorite.size > 0) {
                listDataUserAdapter.listFavorite = dataFavorite
            } else {
                listDataUserAdapter.listFavorite = ArrayList()
                showMessage(getString(R.string.empty))
            }
        }
    }

    private fun showMessage(string: String) {
        Snackbar.make(rv_user, string, Snackbar.LENGTH_SHORT).show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_setting -> {
                val intent = Intent(this@MainActivity, SettingActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

}