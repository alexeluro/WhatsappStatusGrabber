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

    private var listOfViewedStatuses = mutableListOf<File>()
    private var listOfSavedStatuses = mutableListOf<File>()

    private var _loadingState = MutableLiveData<ResultState>()
    val loadingState: LiveData<ResultState>
        get() = _loadingState

    private var _directoryViewedStatusFiles = MutableLiveData<MutableList<File>>()
    val directoryViewedStatusFiles: LiveData<MutableList<File>>
        get() = _directoryViewedStatusFiles

    private var _directorySavedStatusFiles = MutableLiveData<MutableList<File>>()
    val directorySavedStatusFiles: LiveData<MutableList<File>>
        get() = _directorySavedStatusFiles


    fun loadViewedStatuses(directory: File) {
        viewModelScope.launch {
            _loadingState.value = ResultState(Constants.ResultState.LOADING.name, null)
            getViewedStatuses(directory)
        }

    }

    fun loadSavedStatuses(directory: File) {
        viewModelScope.launch {
            _loadingState.value = ResultState(Constants.ResultState.LOADING.name, null)
            getSavedStatuses(directory)
        }
    }


    private fun getViewedStatuses(directory: File) {
        listOfViewedStatuses.clear()
        if (!directory.listFiles().isNullOrEmpty()) {
            for (x in directory.listFiles()!!) {
                if (x.isDirectory) {
                    loadViewedStatuses(directory)
                    _loadingState.value =
                        ResultState(Constants.ResultState.LOADING.name, "Path found: ${x.absolutePath}")
                } else {
                    _loadingState.value =
                        ResultState(Constants.ResultState.LOADING.name, "Path found: ${x.absolutePath}")
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
                "Total files found in ${directory.canonicalPath}: ${listOfViewedStatuses.size}"
            )
            _directoryViewedStatusFiles.postValue(listOfViewedStatuses)
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
                    loadSavedStatuses(directory)
                    _loadingState.value =
                        ResultState(Constants.ResultState.LOADING.name, "Path found: ${x.absolutePath}")
                } else {
                    _loadingState.value =
                        ResultState(Constants.ResultState.LOADING.name, "Path found: ${x.absolutePath}")
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
                "Total files found in ${directory.canonicalPath}: ${listOfSavedStatuses.size}"
            )
            _directorySavedStatusFiles.postValue(listOfSavedStatuses)
        } else {
            _loadingState.value =
                ResultState(Constants.ResultState.SUCCESS.name, "Nothing found here!")
        }
    }


}