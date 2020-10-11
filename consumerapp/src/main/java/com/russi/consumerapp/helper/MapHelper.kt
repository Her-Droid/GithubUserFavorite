package com.russi.consumerapp.helper

import android.database.Cursor
import com.russi.consumerapp.database.GithubDatabase
import com.russi.consumerapp.model.FavoriteModel

object MapHelper {

    fun mapCursorToArrayList(cursor: Cursor?): ArrayList<FavoriteModel> {
        val userList = ArrayList<FavoriteModel>()

        cursor?.apply {
            while (moveToNext()) {
                val username =
                    getString(getColumnIndexOrThrow(GithubDatabase.FavoriteTable.USERNAME))
                val name = getString(getColumnIndexOrThrow(GithubDatabase.FavoriteTable.NAME))
                val avatar = getString(getColumnIndexOrThrow(GithubDatabase.FavoriteTable.AVATAR))
                val company = getString(getColumnIndexOrThrow(GithubDatabase.FavoriteTable.COMPANY))
                val location =
                    getString(getColumnIndexOrThrow(GithubDatabase.FavoriteTable.LOCATION))
                val followers =
                    getInt(getColumnIndexOrThrow(GithubDatabase.FavoriteTable.FOLLOWERS))
                val following =
                    getInt(getColumnIndexOrThrow(GithubDatabase.FavoriteTable.FOLLOWING))
                val favorite =
                    getString(getColumnIndexOrThrow(GithubDatabase.FavoriteTable.FAVORITE))
                userList.add(
                    FavoriteModel(
                        follower = followers.toString(),
                        name = name,
                        avatarUrl = avatar,
                        company = company,
                        location = location,
                        following = following.toString(),
                        login = username,
                        favoriteUser = favorite
                    )
                )
            }
        }
        return userList
    }
}