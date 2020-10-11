package com.russi.githubapi.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FavoriteModel(

    @SerializedName("follower")
    val follower: String = "",

    @SerializedName("following")
    val following: String = "",

    @SerializedName("name")
    var name: String = "",

    @SerializedName("company")
    val company: String = "",

    @SerializedName("location")
    val location: String = "",

    @SerializedName("avatar")
    var avatar: Int = 0,

    @SerializedName("repository")
    val repository: Int = 0,

    @SerializedName("username")
    val username: String = "",

    @SerializedName("login")
    var login: String = "",

    @SerializedName("favorite")
    var favoriteUser: String = "",

    @SerializedName("avatar_url")
    var avatarUrl: String = ""

) : Parcelable
