package com.example.todoonline.utils

sealed class FirebaseResult<T>(val data:T?=null,val message:String?=null) {
    class Loading<T>():FirebaseResult<T>()
    class Success<T>(data: T):FirebaseResult<T>(data)
    class Error<T>(data: T?=null,message: String?):FirebaseResult<T>(data,message)
}