package com.russi.githubapi.helper

import android.database.Cursor
import com.russi.githubapi.database.GithubDatabase
import com.russi.githubapi.model.FavoriteModel

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
                        username,
                        name,
                        avatar,
                        company,
                        location,
                        followers,
                        following,
                        favorite
                    )
                )
            }
        }
        return userList
    }
}