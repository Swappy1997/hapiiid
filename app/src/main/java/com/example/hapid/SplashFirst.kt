package com.example.hapid

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.hapid.databinding.FragmentSplashFirstBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SplashFirst : Fragment() {

    private var _binding: FragmentSplashFirstBinding? = null
    private val binding get() = _binding!!

    private companion object {
        const val SPLASH_DELAY_MS = 2000L //
        const val PREF_KEY_FIRST_TIME = "first_time"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSplashFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences = requireActivity().getPreferences(Context.MODE_PRIVATE)
        val isFirstTime = sharedPreferences.getBoolean(PREF_KEY_FIRST_TIME, true)

        if (isFirstTime) {
            sharedPreferences.edit().putBoolean(PREF_KEY_FIRST_TIME, false).apply()
            GlobalScope.launch(Dispatchers.Main) {
                delay(SPLASH_DELAY_MS)
                findNavController().navigate(R.id.action_splashFirst_to_splashTwo)
            }
        } else {

            GlobalScope.launch(Dispatchers.Main) {
                delay(SPLASH_DELAY_MS)
                findNavController().navigate(R.id.action_splashFirst_to_login)
            }
        }
    }
}