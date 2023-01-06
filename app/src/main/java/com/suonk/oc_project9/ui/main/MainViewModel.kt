package com.suonk.oc_project9.ui.main

import android.net.Uri
import android.util.Log
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    private val _uriLiveData = MutableLiveData<Uri>()
    val uriLiveData: LiveData<Uri> = _uriLiveData

    fun setUriLiveData(uri: Uri) {
        _uriLiveData.value = uri
    }
}