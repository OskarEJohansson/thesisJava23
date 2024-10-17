package dev.oskarjohansson.domain.model

import java.util.Date

data class Review(val reviewId:String,
                  val text:String,
                  val rating:Int,
                  val createdAt:Date,
                  val userId:String,
                  val bookId:String
)
