package com.rahobbs.cats_n_dogs.ui

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.rahobbs.cats_n_dogs.R
import com.rahobbs.cats_n_dogs.databinding.PhotoFragmentBinding

class PhotoFragment : Fragment(), ActivityCompat.OnRequestPermissionsResultCallback {
    private lateinit var binding: PhotoFragmentBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val viewModel: PhotoViewModel by lazy {
        ViewModelProvider(this).get(PhotoViewModel::class.java)
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 434
        fun newInstance() = PhotoFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = PhotoFragmentBinding.inflate(inflater, container, false)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        checkLocationPermissions()

        viewModel.location.observe(viewLifecycleOwner, {
            binding.latLongText.text = it.latitude.toString() + it.longitude.toString()
        })

        viewModel.status.observe(viewLifecycleOwner, Observer {
            when (it) {
                ApiStatus.ERROR -> {
                    binding.animalPhoto.setImageResource(R.drawable.ic_baseline_broken_image_24)
                }
                ApiStatus.LOADING -> {
                    // TODO: show progress spinner
                }
            }
        })

        viewModel.catResult.observe(viewLifecycleOwner, {
            Glide.with(requireContext())
                .load(it.file)
                .into(binding.animalPhoto)
        })

        viewModel.dogResult.observe(viewLifecycleOwner, {
            Glide.with(requireContext())
                .load(it.url)
                .into(binding.animalPhoto)
        })

        viewModel.sunRiseSetResult.observe(viewLifecycleOwner, {
            Log.d("sun", "sunInfo: $it")
            binding.nextEventText.text = it.results.sunrise.toString()
        })

        return binding.root
    }

    private fun checkLocationPermissions() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location: Location? ->
                        location?.let {
                            viewModel.location.postValue(it)
                        }
                    }
            }
            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION) -> {
                // TODO: Provide information to user about permissions
            }
            else -> {
                requestPermissions(
                    arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                    LOCATION_PERMISSION_REQUEST_CODE
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                checkLocationPermissions()
                return
            }
        }
    }
}