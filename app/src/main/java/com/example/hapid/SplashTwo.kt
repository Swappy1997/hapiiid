package com.example.hapid

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.hapid.databinding.FragmentSplashTwoBinding


class SplashTwo : Fragment() {
    private var _binding: FragmentSplashTwoBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSplashTwoBinding.inflate(inflater, container, false)
        binding.getStarted.setOnClickListener {

            findNavController().navigate(R.id.action_splashTwo_to_login)
        }
        return binding.root
    }

}