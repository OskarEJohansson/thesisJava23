package dev.oskarjohansson.domain.model

data class User(val id:String,
                val email:String,
                val username: String,
                val password:String,
                val role: Role,
                val createdAt:String,
                val library:List<LibraryEntry>) {

    data class LibraryEntry(
        val bookid:String,
        val reviewId:String)

}