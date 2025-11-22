package com.example.gitexplorer

import android.app.Application
import com.example.gitexplorer.data.remote.local.AppDatabase

class GitExplorerApp : Application() {

    override fun onCreate() {
        super.onCreate()
        AppDatabase.init(this)
    }
}