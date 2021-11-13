package com.cryoggen.android.vklikes.request

data class RequestVkApi(
    val token:String,
    val userId:String,
    val version:String,
    val extended:String,
    val count:String,
    var offset:String,
) {
}

