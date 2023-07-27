package com.suonk.oc_project9.content_provider

//@AndroidEntryPoint
//class RealEstateContentProvider @Inject constructor(
//    private val realEstateRepository: RealEstateRepository,
//
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
//        // Use the repository to fetch data
//        val realEstates = realEstateRepository.getAllRealEstatesWithPhotos()
//        return null
//    }
//
//    override fun insert(uri: Uri, values: ContentValues?): Uri? {
//        return null
//    }
//}