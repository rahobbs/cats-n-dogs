package com.rahobbs.cats_n_dogs.ui

import android.location.Location
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

    val location = MutableLiveData<Location>()

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun getSunRiseSetData() {
        coroutineScope.launch {
            // TODO: handle case where we haven't successfully gotten location
            val getSunRiseSet =
                SunRiseSetApi.retrofitService.getSunRiseSetAsync(
                    location.value!!.latitude,
                    location.value!!.longitude
                )
            _status.value = ApiStatus.LOADING
            try {
                val sunRiseSetResponse = getSunRiseSet.await()
                _sunRiseSetResult.value = sunRiseSetResponse
                _status.value = ApiStatus.DONE
                getNewAnimalPhoto(sunRiseSetResponse.results.isDaytime())
            } catch (e: Exception) {
                Log.d("sunriseError: " + e.message.toString(), Throwable().toString())
                _status.value = ApiStatus.ERROR
            }
        }
    }

    private fun getNewAnimalPhoto(isDaytime: Boolean) {
        if (isDaytime) {
            getNewCatPic()
        } else {
            getNewDogPic()
        }
    }

    private fun getNewCatPic() {
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

    private fun getNewDogPic() {
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
