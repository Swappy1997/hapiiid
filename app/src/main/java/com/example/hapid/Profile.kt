package com.example.hapid

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.media3.effect.Crop
import com.bumptech.glide.Glide
import com.example.hapid.databinding.FragmentProfileBinding
import java.util.Locale

class Profile : Fragment() {
    private val locationViewModel: LocationViewModel by viewModels()
    private val contract = registerForActivityResult(ActivityResultContracts.GetContent()) {
        binding.profilepic.setImageURI(it)
    }
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        binding.currentlocation.setOnClickListener {
            Log.d("button", "onCreateView: " + "button click")
            onPickLocationClick()
        }
        locationViewModel.locationLiveData.observe(viewLifecycleOwner) { location ->
            updateCityName(location)
        }
        locationViewModel.locationLiveData.observe(viewLifecycleOwner) { location ->
            // Handle the retrieved location
            val latitude = location.latitude
            val longitude = location.longitude
        }
        binding.layout1.setOnClickListener {
            contract.launch("image/*")
        }

        return binding.root
    }

    private fun onPickLocationClick() {
        val context = requireContext()

        if (ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            locationViewModel.fetchCurrentLocation()
        }
    }

    private fun updateCityName(location: Location) {
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        val addresses: MutableList<Address>? = geocoder.getFromLocation(
            location.latitude,
            location.longitude,
            1
        )

        if (addresses != null) {
            if (addresses.isNotEmpty()) {
                val cityName = addresses[0].locality
                binding.currentlocation.text = cityName
            }
        }
    }


    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 123

    }

}