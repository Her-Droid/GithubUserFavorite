package com.russi.consumerapp.database

import android.net.Uri
import android.provider.BaseColumns


object GithubDatabase {
    const val AUTH = "com.russi.githubapi"
    const val SCHEME = "content"

    class FavoriteTable : BaseColumns {
        companion object{
            const val USER_TABLE_NAME = "favorite"
            const val USERNAME = "username"
            const val NAME = "name"
            const val AVATAR = "avatar"
            const val COMPANY = "company"
            const val LOCATION = "location"
            const val FOLLOWERS = "followers"
            const val FOLLOWING = "following"
            const val FAVORITE = "favoriteUser"

            val USER_CONTENT_URI : Uri = Uri.Builder().scheme(SCHEME).authority(AUTH).appendPath(
                USER_TABLE_NAME).build()
        }
    }
}