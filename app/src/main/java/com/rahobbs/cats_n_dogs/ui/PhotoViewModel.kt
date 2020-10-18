package com.rahobbs.cats_n_dogs.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rahobbs.cats_n_dogs.network.CatApi
import com.rahobbs.cats_n_dogs.network.CatResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

enum class ApiStatus { LOADING, ERROR, DONE }

class PhotoViewModel : ViewModel() {
    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _catResult = MutableLiveData<CatResponse>()
    val catResult: LiveData<CatResponse>
        get() = _catResult

    private val _status = MutableLiveData<ApiStatus>()
    val status: LiveData<ApiStatus>
        get() = _status

    init {
        getNewAnimalPhoto()
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private fun getNewAnimalPhoto() {
        coroutineScope.launch {
            val getCatDeferred = CatApi.retrofitService.getNewCatAsync()
            _status.value = ApiStatus.LOADING
            try {
                val catResponse = getCatDeferred.await()
                _catResult.value = catResponse
                _status.value = ApiStatus.DONE
            } catch (e: Exception) {
                _status.value = ApiStatus.ERROR
            }
        }
    }
}