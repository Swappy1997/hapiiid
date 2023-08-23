package com.example.hapid

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.hapid.databinding.DialogCustomOtpBinding
import com.example.hapid.databinding.FragmentLoginBinding


class Login : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    lateinit var viewModel: ProfileViewModel

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[ProfileViewModel::class.java]

        requestOtp()
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    fun requestOtp() {
        binding.requestotp.setOnClickListener {
            val mobileNumber = binding.etphoneno.text.toString()
            viewModel.setMobileNumber(mobileNumber)
            viewModel.generateOtp()
        }
        viewModel.generatedOtp.observe(viewLifecycleOwner) { otp ->
            if (otp != null) {
                // OTP is generated, show the dialog
                showOtpMessageDialog(otp)
            }
        }
    }


    fun showOtpMessageDialog(otp: String) {
        val otpMessageDialogBinding = DialogCustomOtpBinding.inflate(layoutInflater)
        // otpMessageDialogBinding.tvGeneratedOtp.text = viewModel.generatedOtp.value

        val dialog = context?.let {
            AlertDialog.Builder(it)
                .setView(otpMessageDialogBinding.root)
                .setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()
                    findNavController().navigate(R.id.action_login_to_otp)

                }
                .setCancelable(false)
                .create()
        }
        dialog?.show()
        if (otp.length >= 4) {
            otpMessageDialogBinding.pinDigit1.text = otp.substring(0, 1)
            otpMessageDialogBinding.pinDigit2.text = otp.substring(1, 2)
            otpMessageDialogBinding.pinDigit3.text = otp.substring(2, 3)
            otpMessageDialogBinding.pinDigit4.text = otp.substring(3, 4)
        }
    }
}