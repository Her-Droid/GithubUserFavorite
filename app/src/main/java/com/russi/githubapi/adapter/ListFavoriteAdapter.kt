package com.russi.githubapi.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.russi.githubapi.DetailActivity
import com.russi.githubapi.DetailActivity.Companion.EXTRA_DATA
import com.russi.githubapi.FavoriteActivity
import com.russi.githubapi.R
import com.russi.githubapi.model.FavoriteModel
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.list_user.view.*


class ListFavoriteAdapter(favoriteActivity: FavoriteActivity) : RecyclerView.Adapter<ListFavoriteAdapter.ViewHolder>() {

    var listFavorite = mutableListOf<FavoriteModel>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imgUser: CircleImageView = itemView.image_user
        var nameUser: TextView = itemView.name_user
        var context: Context = itemView.context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.list_user, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return listFavorite.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataFavorite: FavoriteModel = listFavorite[position]
        Glide.with(holder.context)
            .load(dataFavorite.avatarUrl)
            .into(holder.imgUser)
        holder.nameUser.text = dataFavorite.login
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.context, DetailActivity::class.java)
            intent.putExtra(EXTRA_DATA, dataFavorite)
            holder.context.startActivity(intent)
        }
    }
}



