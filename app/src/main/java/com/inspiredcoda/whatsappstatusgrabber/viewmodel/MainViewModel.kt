package com.inspiredcoda.whatsappstatusgrabber.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inspiredcoda.whatsappstatusgrabber.utils.Constants
import com.inspiredcoda.whatsappstatusgrabber.utils.ResultState
import kotlinx.coroutines.launch
import java.io.File

class MainViewModel : ViewModel() {

    private var listOfFiles = mutableListOf<File>()

    private var _loadingState = MutableLiveData<ResultState>()
    val loadingState: LiveData<ResultState>
        get() = _loadingState

    private var _directoryFiles = MutableLiveData<MutableList<File>>()
    val directoryFiles: LiveData<MutableList<File>>
        get() = _directoryFiles

    fun loadDirectoryFiles(directory: File) {
        viewModelScope.launch {
            _loadingState.value = ResultState(Constants.ResultState.LOADING.name, null)
            val allFiles = directory.listFiles()
            listOfFiles.clear()
            if (!allFiles.isNullOrEmpty()) {
                for (x in allFiles) {
                    if (x.isDirectory) {
                        loadDirectoryFiles(directory)
                        _loadingState.value = ResultState(Constants.ResultState.LOADING.name, "Path found: ${x.path}")
                    } else {
                        _loadingState.value = ResultState(Constants.ResultState.LOADING.name, "Path found: ${x.path}")
                        val fileName = x.name.toLowerCase()
                        for (y in Constants.VideoConstant.extentions) {
                            if (fileName.endsWith(y, true)) {
                                // add the file to a list
                                listOfFiles.add(x)
                                break
                            }
                        }
                    }
                }

                _directoryFiles.postValue(listOfFiles)
            }

            _loadingState.value = ResultState(Constants.ResultState.SUCCESS.name, "Total files found from ${directory.canonicalPath}: ${listOfFiles.size}")
        }

    }


}