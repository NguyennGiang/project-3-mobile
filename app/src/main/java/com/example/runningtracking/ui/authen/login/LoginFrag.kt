package com.example.runningtracking.ui.authen.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.runningtracking.R
import com.example.runningtracking.base.ViewBindingFragment
import com.example.runningtracking.databinding.FragLoginBinding
import com.example.runningtracking.ui.authen.login.model.LoginRequest
import com.example.runningtracking.utils.SharedPreferencesManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LoginFrag : ViewBindingFragment<FragLoginBinding>() {

    private val viewModel: LoginViewModel by viewModels()

    @Inject
    lateinit var sharedPreferencesManager: SharedPreferencesManager

    override fun getViewBinding(inflater: LayoutInflater, viewGroup: ViewGroup?): FragLoginBinding {
        return FragLoginBinding.inflate(inflater, viewGroup, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding?.apply {
            tvLogin.setOnClickListener {
                val email = etEmail.text.toString()
                val password = etPassword.text.toString()
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(
                        requireContext(),
                        "email and password must not be empty",
                        Toast.LENGTH_LONG
                    ).show()
                    return@setOnClickListener
                }
                viewModel.setEvent(Event.Login(LoginRequest(email, password)))
            }
        }

        observe()
    }

    private fun observe() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.uiState.collect {

                }
            }
        }

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.effect.collect {
                    when (it) {
                        is Effect.Success -> {
                            if (sharedPreferencesManager.isSetupPersonalData) {
                                openFragment(R.id.runFragment)
                            } else {
                                openFragment(R.id.action_loginFrag_to_setupFragment)
                            }
                        }

                        is Effect.Failure -> {
                            Toast.makeText(requireContext(), "Fail to login", Toast.LENGTH_LONG)
                                .show()
                        }
                    }
                }
            }
        }
    }
}