package com.rahobbs.cats_n_dogs.ui

import android.location.Location
import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rahobbs.cats_n_dogs.isDaytime
import com.rahobbs.cats_n_dogs.network.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

enum class ApiStatus { LOADING, ERROR, DONE }

class PhotoViewModel : ViewModel() {
    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    // For LiveData that we want to observe in the view, keep a MutableLiveData private to the
    // ViewModel and expose a public getter for a non-mutable LiveData
    private val _photoUrl = MutableLiveData<String>()
    val photoUrl: LiveData<String>
        get() = _photoUrl

    private val _sunRiseSetResult = MutableLiveData<SunRiseSetResponse>()
    val sunRiseSetResult: LiveData<SunRiseSetResponse>
        get() = _sunRiseSetResult

    private val _status = MutableLiveData<ApiStatus>()
    val status: LiveData<ApiStatus>
        get() = _status

    val location = MutableLiveData<Location>()
    private val timer: CountDownTimer
    private var timeToRefresh = COUNTDOWN_TIME_MS

    init {
        // Initialize location to NYC as a default in case
        // checking the user's location fails or they don't allow location permissions
        location.value = Location("dummy_location")
        location.value!!.latitude = 40.7128
        location.value!!.longitude = -74.0060

        // Initialize a CountDownTimer to always refresh our data every set time period
        timer = object : CountDownTimer(COUNTDOWN_TIME_MS, ONE_SECOND) {

            override fun onTick(millisUntilFinished: Long) {
                timeToRefresh = (millisUntilFinished / ONE_SECOND)
            }

            override fun onFinish() {
                getSunRiseSetData()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun getSunRiseSetData() {
        coroutineScope.launch {
            // Set status to loading until we have all of our data
            _status.value = ApiStatus.LOADING

            val getSunRiseSet =
                SunRiseSetApi.retrofitService.getSunRiseSetAsync(
                    location.value!!.latitude,
                    location.value!!.longitude
                )

            try {
                val sunRiseSetResponse = getSunRiseSet.await()
                _sunRiseSetResult.value = sunRiseSetResponse
                getNewAnimalPhoto(sunRiseSetResponse.results.isDaytime())
            } catch (e: Exception) {
                Log.d("sunriseError: " + e.message.toString(), Throwable().toString())
                _status.value = ApiStatus.ERROR
                // TODO: Show something useful to the user
            }
        }
    }

    private fun getNewAnimalPhoto(isDaytime: Boolean) {
        // Actually start the timer. I'm assuming here that fetching
        // the photos won't take overly long and that we don't mind
        // that counting against our timer
        timer.start()
        if (isDaytime) getNewCatPic()
        else getNewDogPic()
    }

    private fun getNewCatPic() {
        coroutineScope.launch {
            // Ideally, I'd like to move these retrofit service calls to a repository layer
            val getCatDeferred = CatApi.retrofitService.getNewCatAsync()
            try {
                val catResponse = getCatDeferred.await()
                _photoUrl.value = catResponse.url
                _status.value = ApiStatus.DONE
            } catch (e: Exception) {
                _status.value = ApiStatus.ERROR
            }
        }
    }

    private fun getNewDogPic() {
        coroutineScope.launch {
            val getDogDeferred = DogApi.retrofitService.getNewDogAsync()
            try {
                val dogResponse = getDogDeferred.await()
                _photoUrl.value = dogResponse.url
                _status.value = ApiStatus.DONE
            } catch (e: Exception) {
                _status.value = ApiStatus.ERROR
            }
        }
    }

    companion object {
        private const val ONE_SECOND = 1000L
        private const val COUNTDOWN_TIME_MS = 300000L // 5 minutes
    }
}
