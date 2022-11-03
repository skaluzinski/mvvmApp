package com.example.mvvmapp.users.views


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dogsapp.databinding.UsersListFragmentBinding
import com.example.mvvmapp.users.model.User
import com.example.mvvmapp.users.util.Routes
import com.example.mvvmapp.users.util.UiEvent
import com.example.mvvmapp.users.util.UsersListEvent
import com.example.mvvmapp.users.viewmodels.UsersViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UsersListFragment : Fragment() {


    private var _binding: UsersListFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: UsersViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val usersListFragment = UsersListFragmentBinding.inflate(inflater, container, false)
        _binding = usersListFragment
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = binding.usersList
        val usersListAdapter = UsersListAdapter(
            ::userEdit,
            ::userDelete
        )
        recyclerView.adapter = usersListAdapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        binding.fab.setOnClickListener {
            navigate(Routes.ADD_EDIT_USER)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                showHideProgressBar(true)
                viewModel.users.collect {
                    usersListAdapter.submitList(it)
                }
                showHideProgressBar(false)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiEvent.collectLatest { uiState ->
                    when (uiState) {
                        is UiEvent.Navigate -> navigate(uiState.route)
                        is UiEvent.ShowSnackbar -> showSnackbar(uiState.message, uiState.action)
                        is UiEvent.isLoading -> showHideProgressBar(uiState.state)
                    }
                }
            }
        }
    }

    private fun userEdit(user: User) {
        viewModel.onEvent(UsersListEvent.UserClicked(user))
    }

    private fun userDelete(user: User) {
        viewModel.onEvent(UsersListEvent.DeleteUser(user))
    }

    private fun navigate(route: String) = when (route) {

        Routes.ADD_EDIT_USER -> {
            Log.d("dupa","dupa")
            val action = UsersListFragmentDirections.usersListToCreateEditUser("")
            requireView().findNavController().navigate(action)
        }
        else -> {
            Log.d("dupa","dupa")
            if (route.contains(Routes.ADD_EDIT_USER)) {

                val pattern = "\\d+".toRegex()
                val found = pattern.find(route)
                val action = UsersListFragmentDirections.usersListToCreateEditUser(found!!.value)
                requireView().findNavController().navigate(action)
            } else {
                viewModel.onEvent(UsersListEvent.Error(Exception("Action not supported")))
            }
        }
    }

    private fun showHideProgressBar(state: Boolean) {
        if (state) binding.progressBar.visibility =
            View.VISIBLE else binding.progressBar.visibility = View.GONE
    }

    private fun showSnackbar(message: String, action: String? = null) {
        val snackbar = Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT)

        //I know that it's a bad idea to hardcode action in this way,it's just for the excercise
        if (!action.isNullOrBlank()) {
            snackbar.setAction(
                action,
                {
                    viewModel.onEvent(UsersListEvent.OnUndoDeleteUser)
                }
            )
        }
        snackbar.show()
    }

}


