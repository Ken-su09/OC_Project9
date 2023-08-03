package com.suonk.oc_project9.content_provider

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import com.suonk.oc_project9.domain.RealEstateRepository
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent

class RealEstateContentProvider : ContentProvider() {

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface ContentProviderEntryPoint {
        fun getRealEstateRepository(): RealEstateRepository
    }

    override fun onCreate(): Boolean {
        return true
    }

    override fun query(uri: Uri, projection: Array<out String>?, selection: String?, selectionArgs: Array<out String>?, sortOrder: String?): Cursor? {
        val appContext = context?.applicationContext ?: throw IllegalStateException()
        val hiltEntryPoint = EntryPointAccessors.fromApplication(appContext, ContentProviderEntryPoint::class.java)

        val realEstateRepository = hiltEntryPoint.getRealEstateRepository()

        return realEstateRepository.getAllRealEstatesWithPhotosCursor()
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return null
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        return 0
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int {
        return 0
    }
}