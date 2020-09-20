package com.kotlin.githubuser.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    var name : String? = "",
    var userName : String? = "",
    var location : String? = "",
    var repository : String? = "",
    var company : String? = "",
    var following : String? = "",
    var followers : String? = "",
    var avatar : String? = "",
    var id : Int = 0
):Parcelable