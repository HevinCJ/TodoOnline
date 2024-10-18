package com.example.todoonline.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Todo(
    val id: String="",
    val task:String=""
):Parcelable
