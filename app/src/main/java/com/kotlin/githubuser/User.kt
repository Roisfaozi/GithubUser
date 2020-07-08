package com.kotlin.githubuser

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    var name : String = "",
    var userName : String = "",
    var avatar : Int = 0,
    var company : String ="",
    var location : String = "",
    var repository : String = "",
    var followers : String = "",
    var following : String = ""
):Parcelable