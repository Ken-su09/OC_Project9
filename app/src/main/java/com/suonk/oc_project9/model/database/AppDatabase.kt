package com.suonk.oc_project9.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.suonk.oc_project9.model.database.dao.PhotoDao
import com.suonk.oc_project9.model.database.dao.RealEstateDao
import com.suonk.oc_project9.model.database.data.entities.PhotoEntity
import com.suonk.oc_project9.model.database.data.entities.RealEstateEntity

@Database(entities = [RealEstateEntity::class, PhotoEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun realEstateDao(): RealEstateDao
    abstract fun photoDao(): PhotoDao
}