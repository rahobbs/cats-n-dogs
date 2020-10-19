package com.rahobbs.cats_n_dogs.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rahobbs.cats_n_dogs.network.*
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

    private val _dogResult = MutableLiveData<DogResponse>()
    val dogResult: LiveData<DogResponse>
        get() = _dogResult

    private val _sunRiseSetResult = MutableLiveData<SunRiseSetResponse>()
    val sunRiseSetResult: LiveData<SunRiseSetResponse>
        get() = _sunRiseSetResult

    private val _status = MutableLiveData<ApiStatus>()
    val status: LiveData<ApiStatus>
        get() = _status

    init {
        getNewAnimalPhoto()
        getSunRiseSetData()
    }

    private fun getSunRiseSetData() {
        coroutineScope.launch {
            val getSunRiseSet =
                SunRiseSetApi.retrofitService.getSunRiseSetAsync(36.7201600, -4.4203400)
            _status.value = ApiStatus.LOADING
            try {
                val sunRiseSetResponse = getSunRiseSet.await()
                _sunRiseSetResult.value = sunRiseSetResponse
                _status.value = ApiStatus.DONE
            } catch (e: Exception) {
                Log.d("sunriseError: " + e.message.toString(), Throwable().toString())
                _status.value = ApiStatus.ERROR
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private fun getNewAnimalPhoto() {
//        coroutineScope.launch {
//            val getCatDeferred = CatApi.retrofitService.getNewCatAsync()
//            _status.value = ApiStatus.LOADING
//            try {
//                val catResponse = getCatDeferred.await()
//                _catResult.value = catResponse
//                _status.value = ApiStatus.DONE
//            } catch (e: Exception) {
//                _status.value = ApiStatus.ERROR
//            }
//        }
        coroutineScope.launch {
            val getDogDeferred = DogApi.retrofitService.getNewDogAsync()
            _status.value = ApiStatus.LOADING
            try {
                val dogResponse = getDogDeferred.await()
                _dogResult.value = dogResponse
                _status.value = ApiStatus.DONE
            } catch (e: Exception) {
                _status.value = ApiStatus.ERROR
            }
        }
    }


}