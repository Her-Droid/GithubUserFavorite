package com.russi.githubapi.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FavoriteModel(

    @SerializedName("follower")
    var follower: String = "",

    @SerializedName("following")
    var following: String = "",

    @SerializedName("name")
    var name: String = "",

    @SerializedName("company")
    var company: String = "",

    @SerializedName("location")
    var location: String = "",

    @SerializedName("avatar")
    var avatar: Int = 0,

    @SerializedName("repository")
    val repository: Int = 0,

    @SerializedName("username")
    var username: String = "",

    @SerializedName("login")
    var login: String = "",

    @SerializedName("favorite")
    var favoriteUser: String = "",

    @SerializedName("avatar_url")
    var avatarUrl: String = ""

) : Parcelable
