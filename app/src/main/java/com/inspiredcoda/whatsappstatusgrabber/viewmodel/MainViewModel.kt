package com.inspiredcoda.whatsappstatusgrabber.viewmodel

import android.text.format.DateFormat
import android.util.TimeUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inspiredcoda.whatsappstatusgrabber.data.MainRepository
import com.inspiredcoda.whatsappstatusgrabber.data.dao.StatusDao
import com.inspiredcoda.whatsappstatusgrabber.data.entity.Status
import com.inspiredcoda.whatsappstatusgrabber.utils.Constants
import com.inspiredcoda.whatsappstatusgrabber.utils.ResultState
import com.inspiredcoda.whatsappstatusgrabber.utils.toast
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.nio.channels.FileChannel
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*

class MainViewModel(
    private var repository: MainRepository
) : ViewModel() {

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

    var oldViewedStatusList: MutableList<Status>? = null

    fun loadViewedStatuses(directory: File) {
        viewModelScope.launch {
            _loadingState.value = ResultState(Constants.ResultState.LOADING.name, null)
            getViewedStatuses(directory)
        }

    }

    fun loadSavedStatuses(directory: File) {
        _loadingState.value = ResultState(Constants.ResultState.LOADING.name, null)
        getSavedStatuses(directory)
    }


    private fun getViewedStatuses(directory: File) {
        viewModelScope.launch {
            listOfViewedStatuses.clear()
            if (!directory.listFiles().isNullOrEmpty()) {
                for (x in directory.listFiles()!!) {
                    if (x.isDirectory) {
                        loadViewedStatuses(directory)
                        _loadingState.value =
                            ResultState(
                                Constants.ResultState.LOADING.name,
                                "Path found: ${x.absolutePath}"
                            )
                    } else {
                        _loadingState.value =
                            ResultState(
                                Constants.ResultState.LOADING.name,
                                "Path found: ${x.absolutePath}"
                            )
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

                oldViewedStatusList = repository.getAllViewedFileNames()?.toMutableList()

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
    }

    private fun getSavedStatuses(directory: File) {
        listOfSavedStatuses.clear()
        if (!directory.listFiles().isNullOrEmpty()) {
            for (x in directory.listFiles()!!) {
                if (x.isDirectory) {
                    loadSavedStatuses(directory)
                    _loadingState.value =
                        ResultState(
                            Constants.ResultState.LOADING.name,
                            "Path found: ${x.absolutePath}"
                        )
                } else {
                    _loadingState.value =
                        ResultState(
                            Constants.ResultState.LOADING.name,
                            "Path found: ${x.absolutePath}"
                        )
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

    private fun getSimpleDate(): String {
        val simpleFormat = SimpleDateFormat("ddMMyy_", Locale.getDefault())
        return simpleFormat.format(Date())
    }

    fun saveStatus(fileName: String, rootPath: Array<String>, videoSourceFile: File) {

        viewModelScope.launch {
            val newFileName = "${getSimpleDate()}$fileName"

            var destinationFile: File? = null
            var destinationChannel: FileChannel? = null
            var sourceChannel: FileChannel? = null

            for (x in rootPath) {
                destinationFile = File("$x/WhatsApp Status Grabber")
            }

            try {
                val file = File(destinationFile, newFileName)

                sourceChannel = FileInputStream(videoSourceFile).channel
                destinationChannel = FileOutputStream(file).channel

                destinationChannel?.transferFrom(sourceChannel, 0, sourceChannel.size())

            } catch (e: IOException) {
                _loadingState.postValue(
                    ResultState(
                        Constants.ResultState.ERROR.name,
                        "Exception: \n${e.message!!}"
                    )
                )
            } finally {
                val file = File(destinationFile, newFileName)
                if (file.exists()) {
                    _loadingState.postValue(
                        ResultState(
                            Constants.ResultState.SUCCESS.name,
                            "saved successfully"
                        )
                    )

                    repository.saveFileNameToDb(Status(file.name, fileName, false))

                } else {
                    _loadingState.postValue(
                        ResultState(
                            Constants.ResultState.ERROR.name,
                            "Failed to save"
                        )
                    )
                }
                sourceChannel?.close()
                destinationChannel?.close()
            }
        }
    }

    fun saveStatusDetailToDb(singleStatus: Status) {
        viewModelScope.launch {
            repository.saveFileNameToDb(singleStatus)
        }
    }
}