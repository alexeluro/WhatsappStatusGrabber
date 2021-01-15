package com.inspiredcoda.whatsappstatusgrabber.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inspiredcoda.whatsappstatusgrabber.utils.Constants
import com.inspiredcoda.whatsappstatusgrabber.utils.Constants.FileCategory.SAVED_STATUS
import com.inspiredcoda.whatsappstatusgrabber.utils.Constants.FileCategory.VIEWED_STATUS
import com.inspiredcoda.whatsappstatusgrabber.utils.ResultState
import kotlinx.coroutines.launch
import java.io.File

class MainViewModel : ViewModel() {

    private var listOfViewedStatuses = mutableListOf<File>()
    private var listOfSavedStatuses = mutableListOf<File>()

    private var _loadingState = MutableLiveData<ResultState>()
    val loadingState: LiveData<ResultState>
        get() = _loadingState

    private var _directoryFiles = MutableLiveData<MutableList<File>>()
    val directoryFiles: LiveData<MutableList<File>>
        get() = _directoryFiles

    fun loadDirectoryFiles(directory: File, fileCat: Int) {
        viewModelScope.launch {
            _loadingState.value = ResultState(Constants.ResultState.LOADING.name, null)
            val allFiles = directory.listFiles()
            when (fileCat) {
                VIEWED_STATUS -> {
                    getViewedStatuses(directory)
                }

                SAVED_STATUS -> {
                    getSavedStatuses(directory)
                }
            }


        }

    }

    private fun getViewedStatuses(directory: File) {
        listOfViewedStatuses.clear()
        if (!directory.listFiles().isNullOrEmpty()) {
            for (x in directory.listFiles()!!) {
                if (x.isDirectory) {
                    loadDirectoryFiles(directory, VIEWED_STATUS)
                    _loadingState.value =
                        ResultState(Constants.ResultState.LOADING.name, "Path found: ${x.path}")
                } else {
                    _loadingState.value =
                        ResultState(Constants.ResultState.LOADING.name, "Path found: ${x.path}")
                    val fileName = x.name.toLowerCase()
                    for (y in Constants.VideoConstant.extentions) {
                        if (fileName.endsWith(y, true)) {
                            // add the file to a list
                            listOfViewedStatuses.add(x)
                            break
                        }
                    }
                }
            }

            _loadingState.value = ResultState(
                Constants.ResultState.SUCCESS.name,
                "Total files found from ${directory.canonicalPath}: ${listOfViewedStatuses.size}"
            )
            _directoryFiles.postValue(listOfViewedStatuses)
        } else {
            _loadingState.value =
                ResultState(Constants.ResultState.SUCCESS.name, "Nothing found here!")
        }
    }

    private fun getSavedStatuses(directory: File) {
        listOfSavedStatuses.clear()
        if (!directory.listFiles().isNullOrEmpty()) {
            for (x in directory.listFiles()!!) {
                if (x.isDirectory) {
                    loadDirectoryFiles(directory, SAVED_STATUS)
                    _loadingState.value =
                        ResultState(Constants.ResultState.LOADING.name, "Path found: ${x.path}")
                } else {
                    _loadingState.value =
                        ResultState(Constants.ResultState.LOADING.name, "Path found: ${x.path}")
                    val fileName = x.name.toLowerCase()
                    for (y in Constants.VideoConstant.extentions) {
                        if (fileName.endsWith(y, true)) {
                            // add the file to a list
                            listOfSavedStatuses.add(x)
                            break
                        }
                    }
                }
            }

            _loadingState.value = ResultState(
                Constants.ResultState.SUCCESS.name,
                "Total files found from ${directory.canonicalPath}: ${listOfViewedStatuses.size}"
            )
            _directoryFiles.postValue(listOfViewedStatuses)
        } else {
            _loadingState.value =
                ResultState(Constants.ResultState.SUCCESS.name, "Nothing found here!")
        }
    }


}