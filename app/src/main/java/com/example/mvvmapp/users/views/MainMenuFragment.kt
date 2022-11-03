package com.example.mvvmapp.users.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.dogsapp.databinding.MainMenuFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainMenuFragment : Fragment() {
    private var _binding: MainMenuFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val mainMenuFragment = MainMenuFragmentBinding.inflate(inflater, container, false)
        _binding = mainMenuFragment


        binding.addUserButton.setOnClickListener {
            val action = MainMenuFragmentDirections.actionMainMenuToCreateEditUserFragment("")
            requireView().findNavController().navigate(action)
        }

        binding.listButton.setOnClickListener {
            val action  = MainMenuFragmentDirections.actionMainMenuToUsersList()
            requireView().findNavController().navigate(action)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}