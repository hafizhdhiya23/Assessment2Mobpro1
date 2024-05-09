package org.d3if3071.assessment2mobpro1.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import org.d3if3071.assessment2mobpro1.model.Sampah

@Database(entities = [Sampah::class], version = 1, exportSchema = false)
abstract class SampahDb : RoomDatabase(){

    abstract val dao: SampahDao

    companion object {
        @Volatile
        private var INSTANCE: SampahDb? = null
        fun getInstance(context: Context): SampahDb {
            synchronized(this) {
                var instance = INSTANCE

                if(instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        SampahDb::class.java,
                        "sampah.db"
                    ).build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}