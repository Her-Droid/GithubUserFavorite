package com.russi.githubapi.apiservice

import com.russi.githubapi.model.DetailResponse
import com.russi.githubapi.model.FollowModel
import com.russi.githubapi.model.SearchResponse
import com.russi.githubapi.model.UserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("/search/users")
    @Headers("Authorization: token 6311c6085b13054be2c1dea6f7f9e1a0f595547f")
    fun searchUser(
        @Query("q") username:String?
    ):Call<SearchResponse>

    @GET("users")
    @Headers("Authorization: token 6311c6085b13054be2c1dea6f7f9e1a0f595547f")
    fun getUser():Call<UserResponse>

    @GET("users/{username}")
    @Headers("Authorization: token 6311c6085b13054be2c1dea6f7f9e1a0f595547f")
    fun detailUser(
        @Path("username") username: String?
    ):Call<DetailResponse>

    @GET("users/{username}/followers")
    @Headers("Authorization: token 6311c6085b13054be2c1dea6f7f9e1a0f595547f")
    fun getFollowers(
        @Path("username") username: String?
    ): Call<List<FollowModel>>

    @GET("users/{username}/following")
    @Headers("Authorization: token 6311c6085b13054be2c1dea6f7f9e1a0f595547f")
    fun getFollowing(
        @Path("username") username: String?
    ): Call<List<FollowModel>>
}