package com.rahobbs.cats_n_dogs.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.rahobbs.cats_n_dogs.R
import com.rahobbs.cats_n_dogs.databinding.PhotoFragmentBinding

class PhotoFragment : Fragment() {
    private lateinit var binding: PhotoFragmentBinding
    private val viewModel: PhotoViewModel by lazy {
        ViewModelProvider(this).get(PhotoViewModel::class.java)
    }

    companion object {
        fun newInstance() = PhotoFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = PhotoFragmentBinding.inflate(inflater, container, false)

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
            binding.latLongText.text = it.results.sunrise.toString()
        })

        return binding.root
    }
}