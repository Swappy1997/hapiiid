package com.example.hapid

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.hapid.databinding.FragmentOtpBinding
import androidx.lifecycle.observe
import com.example.hapid.databinding.DialogCustomOtpBinding
import kotlinx.coroutines.launch


class Otp : Fragment() {
    private var binding: FragmentOtpBinding? = null
    private val _binding get() = binding!!
    private lateinit var viewModel: ProfileViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOtpBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[ProfileViewModel::class.java]
        _binding.mobile.text = GeneratedOtpSingleton.mobileno
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setEditTextListeners()
        verifyOtp()
        requestOtp()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    private fun setEditTextListeners() {
        setEditTextListeners(
            listOf(
                _binding.pinDigit1,
                _binding.pinDigit2,
                _binding.pinDigit3,
                _binding.pinDigit4
            )
        )
    }

    private fun createTextWatcher(
        current: EditText,
        previous: EditText?,
        next: EditText?
    ): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (s?.length == 1 && next != null) {
                    next.requestFocus()
                } else if (s?.isEmpty() == true) {
                    // If current field becomes empty, clear previous field and focus on it
                    current.text.clear()
                    previous?.requestFocus()
                }
            }
        }
    }


    private fun setEditTextListeners(editTexts: List<EditText>) {
        for (i in editTexts.indices) {
            val current = editTexts[i]
            val previous = editTexts.getOrNull(i - 1)
            val next = editTexts.getOrNull(i + 1)

            current.addTextChangedListener(createTextWatcher(current, previous, next))
        }
    }

    private fun verifyOtp() {
        _binding.submit.setOnClickListener {
            val enteredText1 = _binding.pinDigit1.text.toString()
            val enteredText2 = _binding.pinDigit2.text.toString()
            val enteredText3 = _binding.pinDigit3.text.toString()
            val enteredText4 = _binding.pinDigit4.text.toString()

            val combinedText = enteredText1 + enteredText2 + enteredText3 + enteredText4
            viewModel.setEnteredOtp(combinedText)

            val isOtpVerified = viewModel.verifyOtp()
            if (isOtpVerified) {
                viewModel.resetOtpVerification()
                viewLifecycleOwner.lifecycleScope.launch {
                    findNavController().navigate(R.id.action_otp_to_profile)
                }
            } else {
                showToast("Otp is incorrect, please try again!!")
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    fun requestOtp() {
        _binding.resend.setOnClickListener {
            val mobileNumber = GeneratedOtpSingleton.mobileno

            if (mobileNumber != null) {
                viewModel.setMobileNumber(mobileNumber)
            }
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