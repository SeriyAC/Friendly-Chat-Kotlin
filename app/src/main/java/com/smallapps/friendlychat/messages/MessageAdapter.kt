package com.smallapps.friendlychat.messages

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.smallapps.friendlychat.database.FriendlyMessage
import com.smallapps.friendlychat.databinding.ItemMessageBinding

// RecyclerView Adapter for list of messages
class MessageAdapter :
    ListAdapter<FriendlyMessage, MessageAdapter.MessageViewHolder>(DiffCallBack) {

    // One item layout inflation
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        return MessageViewHolder.from(parent)
    }

    // Binding data to one item
    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    // ViewHolder class for operations with one message
    class MessageViewHolder private constructor(private val binding: ItemMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): MessageViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding =
                    ItemMessageBinding.inflate(inflater, parent, false)
                return MessageViewHolder(binding)
            }
        }

        fun bind(item: FriendlyMessage) {
            binding.message = item
            binding.executePendingBindings()
        }

    }

    // DiffCallback singleton for checking difference between items
    private object DiffCallBack : DiffUtil.ItemCallback<FriendlyMessage>() {
        override fun areItemsTheSame(oldItem: FriendlyMessage, newItem: FriendlyMessage): Boolean {
            return newItem === oldItem
        }

        override fun areContentsTheSame(
            oldItem: FriendlyMessage,
            newItem: FriendlyMessage
        ): Boolean {
            return newItem == oldItem
        }

    }
}