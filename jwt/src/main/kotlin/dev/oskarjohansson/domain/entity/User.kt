package dev.oskarjohansson.domain.entity

import com.mongodb.internal.connection.Time
import org.apache.tomcat.jni.Library

data class User(val id: String, val email:String, val username:String, val role: Role, val dateJoined:Time, val library: Library) {
}