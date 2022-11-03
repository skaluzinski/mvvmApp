package com.example.mvvmapp.users.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.dogsapp.databinding.CreateEditUserBinding
import com.example.mvvmapp.users.model.User
import com.example.mvvmapp.users.util.UiEvent
import com.example.mvvmapp.users.util.UsersListEvent
import com.example.mvvmapp.users.viewmodels.UsersViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class CreateEditUserFragment : Fragment() {

    private var _binding: CreateEditUserBinding? = null
    private val binding get() = _binding!!
    private val viewModel: UsersViewModel by viewModels()

    private lateinit var userId: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val createEditFragment =
            CreateEditUserBinding.inflate(inflater, container, false)
        _binding = createEditFragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            userId = it.getString("userId").toString()
        }

        viewLifecycleOwner.lifecycleScope.launch{
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiEvent.collect(){state->
                    when(state){
                        is UiEvent.ShowSnackbar -> showSnackbar(state.message)
                        else -> {
                            viewModel.onEvent(UsersListEvent.Error(Exception("Unsuported acction")))
                        }
                    }

                }
            }
        }

        if (userId.isNotEmpty()) {
            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.getUserById(userId).collect() { user ->
                        binding.name.setText(user.name)
                        binding.surname.setText(user.surname)
                        binding.note.setText(user.note)
                        binding.adminCheckbox.isChecked = user.isAuthorised
                        viewModel.onEvent(UsersListEvent.OnUserChangeDone(user, true))
                    }
                }

            }


        }

        binding.addUserButton.setOnClickListener {
            if (binding.note.text!!.isNotBlank()
                && binding.surname.text!!.isNotBlank()
                && binding.name.text!!.isNotBlank()
            ) {
                viewModel.onEvent(
                    UsersListEvent.AddUser(
                        User(
                            name = binding.name.text.toString(),
                            surname = binding.surname.text.toString(),
                            note = binding.note.text.toString(),
                            isAuthorised = binding.adminCheckbox.isChecked
                        )
                    )
                )


                //Decided to hardcode snackbar instead of changing uiState in viewmodel
                showSnackbar("New user created")
            }
            else{
                viewModel.onEvent(UsersListEvent.Error(Exception("Incorrect user")))
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showSnackbar(message: String){
        Snackbar.make(
            binding.root,
            message,
            Snackbar.LENGTH_SHORT
        ).show()

    }

}