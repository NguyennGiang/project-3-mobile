package com.example.runningtracking.ui.setup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.runningtracking.R
import com.example.runningtracking.base.ViewBindingFragment
import com.example.runningtracking.databinding.FragSetupBinding
import com.example.runningtracking.utils.SharedPreferencesManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SetupFrag: ViewBindingFragment<FragSetupBinding>() {

    @Inject
    lateinit var sharedPreferencesManager: SharedPreferencesManager

    override fun getViewBinding(inflater: LayoutInflater, viewGroup: ViewGroup?): FragSetupBinding {
        return FragSetupBinding.inflate(inflater, viewGroup, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding?.apply {
            tvContinue.setOnClickListener {

                val success = setupPersonalData()
                if (success){
                    openFragment(R.id.action_setupFragment_to_runFragment)
                }
                else {
                    Snackbar.make(requireView(), "Fill all the information", Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun setupPersonalData() : Boolean{
        val name = binding?.etName?.text.toString()
        val weight = binding?.etWeight?.text.toString()
        if(name.isEmpty() || weight.isEmpty()) {
            return false
        }
        sharedPreferencesManager.isSetupPersonalData = true
        sharedPreferencesManager.userName = name
        sharedPreferencesManager.userWeight = weight.toFloat()
        return true
    }
}