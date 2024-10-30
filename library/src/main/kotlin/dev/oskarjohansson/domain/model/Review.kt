package dev.oskarjohansson.domain.model

import org.springframework.data.mongodb.core.mapping.Document
import java.util.Date

@Document(collection = "Reviews")
data class Review(val reviewId:String,
                  val text:String,
                  val rating:Int,
                  val createdAt:Date,
                  val userId:String,
                  val bookId:String
)
