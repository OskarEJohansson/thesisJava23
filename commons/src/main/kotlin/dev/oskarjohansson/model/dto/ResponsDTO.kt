package dev.oskarjohansson.model.dto

data class ResponseDTO<T>(val status: Int, val message:String? = null, val data:T? = null)
