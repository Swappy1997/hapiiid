package com.example.hapid

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputBinding
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModelProvider
import com.example.hapid.databinding.ActivityMainBinding
import com.example.hapid.databinding.DialogCustomOtpBinding


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var viewModel: ProfileViewModel



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        viewModel = ViewModelProvider(this)[ProfileViewModel::class.java]

//        binding.requestotp.setOnClickListener {

//        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }


    private fun showOtpMessageDialog() {
        val otpMessageDialogBinding = DialogCustomOtpBinding.inflate(layoutInflater)
        // otpMessageDialogBinding.tvGeneratedOtp.text = viewModel.generatedOtp.value

        val dialog = AlertDialog.Builder(this)
            .setView(otpMessageDialogBinding.root)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .setCancelable(false)
            .create()
        dialog.show()
    }
}