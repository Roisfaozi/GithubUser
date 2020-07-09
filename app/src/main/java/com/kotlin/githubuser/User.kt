package com.kotlin.githubuser

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    var userName : String = "",
    var name : String = "",
    var location : String = "",
    var repository : String = "",
    var company : String ="",
    var following : String = "",
    var followers : String = "",
    var avatar : Int = 0
):Parcelable