package com.example.hapid

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    private val _mobileNumber = MutableLiveData<String>()
    val mobileNumber: LiveData<String> = _mobileNumber

    private val _generatedOtp = MutableLiveData<String>()
    val generatedOtp: LiveData<String> = _generatedOtp

    private val _enteredOtp = MutableLiveData<String?>()
    val enteredOtp: LiveData<String?> = _enteredOtp

    private val _verifyOtp = MutableLiveData<Boolean?>()
    val verifyOtp: LiveData<Boolean?> = _verifyOtp
    fun setMobileNumber(number: String) {
        _mobileNumber.value = number
        GeneratedOtpSingleton.mobileno = number


    }

    fun setEnteredOtp(otp: String) {
        _enteredOtp.value = otp
    }

    fun generateOtp() {
        val number = _mobileNumber.value
        if (number != null && number.length >= 4) {
            viewModelScope.launch(Dispatchers.Default) {
                val otp = number.substring(0, 2) + number.substring(number.length - 2)
                GeneratedOtpSingleton.generatedOtp = otp
                Log.d("Generated OTP", otp) // Log the generated OTP
                _generatedOtp.postValue(otp)

            }
        }
    }

    fun verifyOtp(): Boolean {
        val generated = GeneratedOtpSingleton.generatedOtp
        val entered = _enteredOtp.value
        val result = generated != null && entered != null && generated == entered
        _verifyOtp.value = result
        return result
    }
    fun resetOtpVerification() {
        _enteredOtp.value = null // Reset entered OTP
        _verifyOtp.value = null // Reset verification result
    }

}

object GeneratedOtpSingleton {
    var generatedOtp: String? = null
    var mobileno: String? = null
}


