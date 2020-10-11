package com.russi.consumerapp.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DetailResponse(

    @SerializedName("gists_url")
    val gistsUrl: String,

    @SerializedName("repos_url")
    val reposUrl: String,

    @SerializedName("following_url")
    val followingUrl: String,

    @SerializedName("twitter_username")
    val twitterUsername: String,

    @SerializedName("bio")
    val bio: String,

    @SerializedName("created_at")
    val createdAt: String,

    @SerializedName("login")
    val login: String,

    @SerializedName("type")
    val type: String,

    @SerializedName("blog")
    val blog: String,

    @SerializedName("subscriptions_url")
    val subscriptionsUrl: String,

    @SerializedName("updated_at")
    val updatedAt: String,

    @SerializedName("site_admin")
    val siteAdmin: Boolean,

    @SerializedName("company")
    val company: String,

    @SerializedName("id")
    val id: Int,

    @SerializedName("public_repos")
    val publicRepos: Int,

    @SerializedName("gravatar_id")
    val gravatarId: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("organizations_url")
    val organizationsUrl: String,

    @SerializedName("hireable")
    val hireable: String,

    @SerializedName("starred_url")
    val starredUrl: String,

    @SerializedName("followers_url")
    val followersUrl: String,

    @SerializedName("public_gists")
    val publicGists: Int,

    @SerializedName("url")
    val url: String,

    @SerializedName("received_events_url")
    val receivedEventsUrl: String,

    @SerializedName("followers")
    val followers: Int,

    @SerializedName("avatar_url")
    val avatarUrl: String,

    @SerializedName("events_url")
    val eventsUrl: String,

    @SerializedName("html_url")
    val htmlUrl: String,

    @SerializedName("following")
    val following: Int,

    @SerializedName("name")
    val name: String,

    @SerializedName("location")
    val location: String,

    @SerializedName("node_id")
    val nodeId: String
) : Parcelable
