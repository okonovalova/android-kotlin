package ru.netology.nmedia.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.netology.nmedia.data.model.PostInfoEntity


@Database(entities = [PostInfoEntity::class], version = 1, exportSchema = false)
abstract class RoomDb : RoomDatabase() {
    abstract fun postDao(): PostDao

    companion object {
        @Volatile
        private var instance: RoomDb? = null

        fun getInstance(context: Context): RoomDb {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context, RoomDb::class.java, "room.db")
                .fallbackToDestructiveMigration()
                .build()
    }
}