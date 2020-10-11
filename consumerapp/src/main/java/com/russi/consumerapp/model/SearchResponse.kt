package com.russi.consumerapp.model

import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable


@Parcelize
data class SearchResponse(

    @SerializedName("total_count")
    val totalCount: Int,

    @SerializedName("incomplete_results")
    val incompleteResults: Boolean,

    @SerializedName("items")
    val items: List<DataUser>
) : Parcelable

