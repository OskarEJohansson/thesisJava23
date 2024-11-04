package dev.oskarjohansson.model

data class ResponseDTO<T>(val status: Int, val message:String? = null, val data:T? = null)
