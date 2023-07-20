package com.suonk.oc_project9.content_provider

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import com.suonk.oc_project9.model.database.AppDatabase
import com.suonk.oc_project9.model.database.dao.PhotoDao
import com.suonk.oc_project9.model.database.dao.RealEstateDao
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
//
//@AndroidEntryPoint
//class RealEstateContentProvider @Inject constructor(
//    private val appDatabase: AppDatabase,
//    private val realEstateDao: RealEstateDao,
//    private val photoDao: PhotoDao
//) : ContentProvider() {
//
//    override fun onCreate(): Boolean {
//        return true
//    }
//
//    override fun query(
//        uri: Uri,
//        projection: Array<out String>?,
//        selection: String?,
//        selectionArgs: Array<out String>?,
//        sortOrder: String?
//    ): Cursor? {
//        TODO("Not yet implemented")
//    }
//
//    override fun getType(uri: Uri): String? {
//        TODO("Not yet implemented")
//    }
//
//    override fun insert(uri: Uri, values: ContentValues?): Uri? {
//        TODO("Not yet implemented")
//    }
//
//    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
//        TODO("Not yet implemented")
//    }
//
//    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int {
//        TODO("Not yet implemented")
//    }
//}