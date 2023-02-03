package com.suonk.oc_project9.ui.real_estates.modern_storage

import android.content.Context
import android.os.Environment
import android.provider.MediaStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.modernstorage.storage.AndroidFileSystem
import com.google.modernstorage.storage.toOkioPath
import com.suonk.oc_project9.ui.real_estates.details.DetailsPhotoViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okio.source
import javax.inject.Inject

@HiltViewModel
class ModernStorageViewModel @Inject constructor(@ApplicationContext private val context: Context) :
    ViewModel() {

    private val fileSystem = AndroidFileSystem(context)
    private val _addedFile = MutableStateFlow<DetailsPhotoViewState?>(null)
    val addedFile: StateFlow<DetailsPhotoViewState?> = _addedFile

    private fun clearAddedFile() {
        _addedFile.value = null
    }

    fun addMedia() = viewModelScope.launch {
        val extension = "jpg"
        val mimeType = "image/jpeg"
        val collection = MediaStore.Images.Media.getContentUri("external")
        val directory =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)

        val uri = fileSystem.createMediaStoreUri(
            filename = "added-${System.currentTimeMillis()}.$extension",
            collection = collection,
            directory = directory.absolutePath
        ) ?: return@launch clearAddedFile()

        val path = uri.toOkioPath()

        fileSystem.write(path, false) {
            context.assets.open("sample.$extension").source().use { source ->
                writeAll(source)
            }
        }
        fileSystem.scanUri(uri, mimeType)

        val metadata = fileSystem.metadataOrNull(path) ?: return@launch clearAddedFile()
//        _addedFile.value = PhotoViewState(uri, path, metadata)
    }
}