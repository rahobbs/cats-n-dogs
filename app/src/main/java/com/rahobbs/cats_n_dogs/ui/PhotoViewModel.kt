package com.rahobbs.cats_n_dogs.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rahobbs.cats_n_dogs.network.CatApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class PhotoViewModel : ViewModel() {
    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _catResult = MutableLiveData<String>()
    val catResult: LiveData<String>
        get() = _catResult

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
            try {
                _catResult.value = getCatDeferred.await()
            } catch (e: Exception) {
                _catResult.value = "Network Exception: ${e.message}"
            }
        }
    }
}