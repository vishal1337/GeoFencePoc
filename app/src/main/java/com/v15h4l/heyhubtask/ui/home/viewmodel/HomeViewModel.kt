package com.v15h4l.heyhubtask.ui.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.distinctUntilChanged

/**
 * ViewModel to Store Selected File Information
 */
class HomeViewModel : ViewModel() {

    val FILE_1 = "hub.json"
    val FILE_2 = "hub2.json"
    val FILE_3 = "hub3.json"

    enum class DataFiles { F1, F2, F3 }

    private val _fileName = MutableLiveData<String>()
    val fileName: LiveData<String> = _fileName.distinctUntilChanged()

    fun selectFile(dataFiles: DataFiles) {
        when (dataFiles) {
            DataFiles.F1 -> _fileName.postValue(FILE_1)
            DataFiles.F2 -> _fileName.postValue(FILE_2)
            DataFiles.F3 -> _fileName.postValue(FILE_3)
        }
    }
}