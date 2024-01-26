package com.beatareka.jwtlogin.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.beatareka.jwtlogin.R
import com.beatareka.jwtlogin.databinding.FragmentLoadingBinding
import com.beatareka.jwtlogin.viewmodel.LoadingViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoadingFragment : Fragment() {
    private var _binding: FragmentLoadingBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LoadingViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoadingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.checkLoginStatus()
        viewModel.loggedIn.observe(viewLifecycleOwner) { loggedIn ->
            if (loggedIn) {
                findNavController().navigate(R.id.action_LoadingFragment_to_HomeFragment)
            } else {
                findNavController().navigate(R.id.action_LoadingFragment_to_LoginFragment)
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}