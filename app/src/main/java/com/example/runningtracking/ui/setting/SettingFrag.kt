package com.example.runningtracking.ui.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.runningtracking.R
import com.example.runningtracking.base.ViewBindingFragment
import com.example.runningtracking.databinding.FragSettingBinding
import com.example.runningtracking.utils.SharedPreferencesManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingFrag: ViewBindingFragment<FragSettingBinding>() {

    @Inject
    lateinit var sharedPreferencesManager: SharedPreferencesManager

    override fun getViewBinding(
        inflater: LayoutInflater,
        viewGroup: ViewGroup?
    ): FragSettingBinding {
        return FragSettingBinding.inflate(inflater, viewGroup, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding?.apply {
            etName.setText(sharedPreferencesManager.userName)
            etWeight.setText(sharedPreferencesManager.userWeight.toString())

            btnApplyChanges.setOnClickListener {
                val isSuccess = applyChangePersonalData()
                if (isSuccess){
                    Snackbar.make(requireView(), "Save changed", Snackbar.LENGTH_LONG).show()
                    openFragment(R.id.runFragment)
                }
                else {
                    Snackbar.make(requireView(), "Some thing went wrong", Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun applyChangePersonalData(): Boolean {
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