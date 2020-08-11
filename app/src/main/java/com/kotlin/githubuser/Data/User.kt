package com.kotlin.githubuser.Data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    var name : String = "",
    var userName : String = "",
    var location : String = "",
    var repository : String = "",
    var company : String ="",
    var following : String = "",
    var followers : String = "",
    var avatar : String = ""
):Parcelable