package com.sanskar.unilink



import android.app.Application


class UniLinkApp : Application() {
    override fun onCreate() {
        super.onCreate()
        SharedPrefManager.init(this)
    }
}