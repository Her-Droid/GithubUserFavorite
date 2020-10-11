package com.russi.githubapi.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.russi.githubapi.database.FavoriteHelper
import com.russi.githubapi.database.GithubDatabase.AUTH
import com.russi.githubapi.database.GithubDatabase.FavoriteTable.Companion.USER_CONTENT_URI
import com.russi.githubapi.database.GithubDatabase.FavoriteTable.Companion.USER_TABLE_NAME


class FavoriteProvider : ContentProvider() {

    companion object {
        private const val FAVORITE = 1
        private const val FAVORITE_ID = 2
        private val uriMath = UriMatcher(UriMatcher.NO_MATCH)
        private lateinit var favoriteHelper: FavoriteHelper

        init {
            uriMath.addURI(AUTH, USER_TABLE_NAME, FAVORITE)
            uriMath.addURI(AUTH, "$USER_TABLE_NAME/#", FAVORITE_ID)
        }
    }


    override fun onCreate(): Boolean {
        favoriteHelper = FavoriteHelper.getInstance(context as Context)
        favoriteHelper.open()
        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        return when (uriMath.match(uri)) {
            FAVORITE -> favoriteHelper.queryAllUserFavorite()
            FAVORITE_ID -> favoriteHelper.queryByIdUserFavorite(uri.lastPathSegment.toString())
            else -> null
        }
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val context = context ?: return null
        val added: Long = when (FAVORITE) {
            uriMath.match(uri) ->
                favoriteHelper.insertUserFavorite(values!!)
            else -> 0
        }
        context.contentResolver?.notifyChange(USER_CONTENT_URI, null)
        return Uri.parse("$USER_CONTENT_URI/$added")
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val context = context ?: return 0
        val delete: Int = when (FAVORITE_ID) {
            uriMath.match(uri) -> favoriteHelper.deleteByIdUserFavorite(uri.lastPathSegment.toString())
            else -> 0
        }
        context.contentResolver?.notifyChange(USER_CONTENT_URI, null)
        return delete
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
        val update : Int = when (FAVORITE_ID) {
            uriMath.match(uri) -> favoriteHelper.updateUseFavorite(uri.lastPathSegment.toString(), values)
            else -> 0
        }
        context?.contentResolver?.notifyChange(USER_CONTENT_URI, null)
        return update
    }

}