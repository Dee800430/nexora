package com.nexora.app

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform