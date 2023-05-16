package com.temp.randomfact.data.repository.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.temp.randomfact.data.model.FactInfo

@Database(entities = [FactInfo::class], version = 1)
@TypeConverters(Converters::class)
abstract class FactsDatabase : RoomDatabase() {
    abstract fun factsDao(): FactsDao

    companion object {
        @Volatile
        private var INSTANCE: FactsDatabase? = null

        fun getDatabase(context: Context): FactsDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FactsDatabase::class.java,
                    "facts_db"
                ).build()

                INSTANCE = instance
                return instance
            }
        }
    }
}
