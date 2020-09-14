package com.kotlin.githubuser.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    var name : String? = null,
    var userName : String? = null,
    var location : String? = null,
    var repository : String? = null,
    var company : String? = null,
    var following : String? = null,
    var followers : String? = null,
    var avatar : String? = null,
    var id : Int = 0
):Parcelable