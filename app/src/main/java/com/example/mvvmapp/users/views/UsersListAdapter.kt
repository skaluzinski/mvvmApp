package com.example.mvvmapp.users.views

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.dogsapp.databinding.UsersListItemBinding
import com.example.mvvmapp.users.model.User

class UsersListAdapter(
    private val onUserClicked: (User) -> Unit,
    private val onUserDeleted: (User) -> Unit
) : ListAdapter<User,
        UsersListAdapter.ViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return (oldItem.id == newItem.id)
                    && (oldItem.name == newItem.name)
                    && (oldItem.note == newItem.note)
                    && (oldItem.surname == newItem.surname)
                    && (oldItem.isAuthorised == newItem.isAuthorised)
        }
    }

    class ViewHolder(private var binding: UsersListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User,onUserClicked: (User) -> Unit,onUserDeleted: (User) -> Unit) {
            binding.root.setOnClickListener {
                onUserClicked(user)
            }
            binding.root.setOnLongClickListener {
                onUserDeleted(user)
                true
            }
            binding.name.text = user.name
            binding.surname.text = user.surname
            if(user.isAuthorised){
                binding.adminText.text = "Admin"
            }
            else{
                binding.adminText.text = "Not admin"
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewHolder = ViewHolder(
            UsersListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
        return viewHolder
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user,onUserClicked,onUserDeleted)
    }
}