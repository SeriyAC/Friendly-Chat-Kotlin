package com.smallapps.friendlychat.messages

import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.smallapps.friendlychat.database.ChatAPI
import com.smallapps.friendlychat.database.FriendlyMessage
import com.smallapps.friendlychat.databinding.FragmentMessagesBinding
import kotlinx.coroutines.delay

// Main chat fragment
class MessagesFragment : Fragment() {

    // ViewModel lazy initialization
    private val viewModel: MessagesViewModel by lazy {
        ViewModelProvider(this).get(MessagesViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        // Inflation and data binding setting
        val binding =
            FragmentMessagesBinding.inflate(inflater, container, false)

        //Binding ViewModel to view
        binding.viewModel = viewModel

        // Setting lifecycle owner for observers to work
        binding.lifecycleOwner = this

        // Setting RecyclerView Adapter for list of messages
        val adapter = MessageAdapter()
        binding.messageListView.adapter = adapter

        // TextChangeListener for input message field to enable or disable send button
        binding.messageEditText.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.toString().trim().isNotEmpty()) {
                    viewModel.enableSendButton()
                } else {
                    viewModel.disableSendButton()
                }
            }
        })

        // Observer for handling message sending
        viewModel.sendingMessage.observe(viewLifecycleOwner, Observer {
            if (it) {
                ChatAPI.sendMessage(
                    FriendlyMessage(
                        binding.messageEditText.text.toString(),
                        viewModel.username,
                        null))
                binding.messageEditText.setText("")
                viewModel.onSendMessageCompleted()
            }
        })

        // Observer for handling image picking
        viewModel.pickingImage.observe(viewLifecycleOwner, Observer {
            if (it) {
                // TODO
                viewModel.onPickImageCompleted()
            }
        })

        // Observer for updating list
        viewModel.friendlyMessages.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
                Handler().postDelayed(Runnable {
                        binding.messageListView.smoothScrollToPosition(0)
                }, 200)
            }
        })


        //Listener for new data
        ChatAPI.messagesReference.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val message = p0.getValue(FriendlyMessage::class.java)
                viewModel.addMessage(message)
            }

            override fun onChildRemoved(p0: DataSnapshot) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })


        return binding.root
    }


}
