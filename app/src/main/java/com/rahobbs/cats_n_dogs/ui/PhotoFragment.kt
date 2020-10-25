package com.rahobbs.cats_n_dogs.ui

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.View.*
import androidx.fragment.app.Fragment
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.rahobbs.cats_n_dogs.R
import com.rahobbs.cats_n_dogs.databinding.PhotoFragmentBinding
import com.rahobbs.cats_n_dogs.formattedLatitude
import com.rahobbs.cats_n_dogs.formattedLongitude
import com.rahobbs.cats_n_dogs.nextSolarEventTimeString

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
        setHasOptionsMenu(true)

        checkLocationPermissions()

        viewModel.location.observe(viewLifecycleOwner, {
            binding.latLongText.text = getString(
                R.string.current_location,
                it.formattedLatitude(),
                it.formattedLongitude()
            )
        })

        viewModel.status.observe(viewLifecycleOwner, {
            when (it) {
                ApiStatus.ERROR -> {
                    binding.fullScreenProgress.visibility = INVISIBLE
                    binding.animalPhoto.setImageResource(R.drawable.ic_baseline_broken_image_24)
                }
                ApiStatus.LOADING -> {
                    binding.fullScreenProgress.visibility = VISIBLE
                }
                ApiStatus.DONE -> {
                    binding.fullScreenProgress.visibility = INVISIBLE
                }
            }
        })

        viewModel.photoUrl.observe(viewLifecycleOwner, {
            Glide.with(requireContext())
                .load(it)
                .into(binding.animalPhoto)
        })

        viewModel.sunRiseSetResult.observe(viewLifecycleOwner, {
            Log.d("sun", "sunInfo: $it")
            binding.nextEventText.text =
                getString(R.string.next_solar_event, it.results.nextSolarEventTimeString())
        })

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.photo_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.refresh_photo -> {
                viewModel.getSunRiseSetData()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun checkLocationPermissions() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location: Location? ->
                        location?.let {
                            viewModel.location.value = it
                            viewModel.getSunRiseSetData()
                        }
                    }
            }
            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION) -> {
                /* TODO: Provide some information to the user about why we need these permissions.
                * As a fallback, we could randomly choose to show a cat or dog, or hard code a default
                * location.
                * */
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
