package com.reformistsinstitute.coredraft

import android.app.Application
import com.reformistsinstitute.coredraft.di.AppContainer

class CoreDraftApplication : Application() {
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}
