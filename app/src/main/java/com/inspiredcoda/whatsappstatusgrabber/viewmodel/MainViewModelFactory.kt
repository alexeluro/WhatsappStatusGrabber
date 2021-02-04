package com.inspiredcoda.whatsappstatusgrabber.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.inspiredcoda.whatsappstatusgrabber.data.MainRepository

class MainViewModelFactory(
    val repository: MainRepository
) : ViewModelProvider.NewInstanceFactory(){


    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(repository) as T
    }


}