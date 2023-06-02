package com.suonk.oc_project9.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.suonk.oc_project9.model.database.dao.PhotoDao
import com.suonk.oc_project9.model.database.dao.RealEstateDao
import com.suonk.oc_project9.model.database.data.entities.real_estate.PhotoEntity
import com.suonk.oc_project9.model.database.data.entities.real_estate.RealEstateEntity
import com.suonk.oc_project9.utils.Converters

@Database(entities = [RealEstateEntity::class, PhotoEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun realEstateDao(): RealEstateDao
    abstract fun photoDao(): PhotoDao

//    companion object {
//        const val DATABASE_NAME = "app_database"
//
//        val MIGRATION_1_TO_2: Migration = object : Migration(1, 2) {
//            override fun migrate(database: SupportSQLiteDatabase) {
//
//            }
//        }
//    }
}