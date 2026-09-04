package com.example.srs

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform