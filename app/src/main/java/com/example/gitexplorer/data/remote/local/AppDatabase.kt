package com.example.gitexplorer.data.remote.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room





@Database(
    entities = [FavoriteEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : androidx.room.RoomDatabase() {
    abstract fun repoDao(): RepoDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null
        fun init(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "gitexplorer_db"
                ).fallbackToDestructiveMigration()
                    .build()
            }
        }

        fun get(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "gitexplorer_db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}