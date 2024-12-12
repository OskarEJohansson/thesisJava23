package dev.oskarjohansson.service


fun tokenPattern(token:String): Boolean {
    val regex = Regex("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$")

    return regex.matches(token)
}