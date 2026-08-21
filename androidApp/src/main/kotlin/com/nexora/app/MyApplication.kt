// androidMain/kotlin/com/nexora/app/MyApplication.kt

package com.nexora.app

import android.app.Application
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.network.okhttp.OkHttpNetworkFetcherFactory

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Simple Coil initialization with OkHttp network support
        SingletonImageLoader.setSafe { context ->
            ImageLoader.Builder(context)
                .components {
                    add(OkHttpNetworkFetcherFactory())
                }
                .build()
        }
    }
}
