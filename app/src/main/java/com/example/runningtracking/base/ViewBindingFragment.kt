package com.example.runningtracking.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation.findNavController
import androidx.viewbinding.ViewBinding


abstract class ViewBindingFragment<VB: ViewBinding>: Fragment() {
    var binding: VB? = null

    abstract fun getViewBinding(inflater: LayoutInflater, viewGroup: ViewGroup?): VB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.binding = getViewBinding(inflater, container)
        return this.binding?.root
    }

    protected open fun openFragment(destination: Int, bundle: Bundle? = null) {
        findNavController(binding!!.root).navigate(destination, bundle)
    }



    override fun onDestroy() {
        super.onDestroy()
        this.binding = null
    }
}