package com.rahobbs.cats_n_dogs.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
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

        viewModel.catResult.observe(viewLifecycleOwner, Observer {
            print("photoResult$it")
            // TODO: convert result to URI and pass to Glide
        })
        return binding.root
    }
}