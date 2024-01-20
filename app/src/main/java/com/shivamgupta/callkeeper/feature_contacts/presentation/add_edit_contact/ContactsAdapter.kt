package com.shivamgupta.callkeeper.feature_contacts.presentation.add_edit_contact


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.shivamgupta.callkeeper.R
import com.shivamgupta.callkeeper.databinding.LayoutContactCardBinding
import com.shivamgupta.callkeeper.databinding.LayoutEmptyContactsBinding
import com.shivamgupta.callkeeper.feature_contacts.domain.model.Contact
import com.shivamgupta.callkeeper.feature_contacts.util.ResourceProvider

class ContactsAdapter(
    private val items: List<Contact>,
    private val onContactClicked: (Contact) -> Unit,
    private val onItemLongPressed: (Contact) -> Unit,
    private val onContactChecked: (Int, Boolean) -> Unit
) : RecyclerView.Adapter<ViewHolder>() {

    inner class DataViewHolder(val binding: LayoutContactCardBinding) :
        ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                adapterPosition.takeIf { it != RecyclerView.NO_POSITION }?.let { position ->
                    onContactClicked(items[position])
                }
            }

            binding.root.setOnLongClickListener {
                adapterPosition.takeIf { it != RecyclerView.NO_POSITION }?.let { position ->
                    onItemLongPressed.invoke(items[position])
                }

                true
            }

            binding.contactSelectIv.apply {
                setOnClickListener {
                    val position = adapterPosition.takeIf { it != RecyclerView.NO_POSITION } ?: return@setOnClickListener
                    tag = if (tag == "unchecked") "checked" else "unchecked"
                    setImageResource(if (tag == "unchecked") R.drawable.ic_circle else R.drawable.ic_circle_check)

                    val isSelected = (tag == "checked")
                    onContactChecked(position, isSelected)
                }
            }

        }

        fun bind(item: Contact) {
            binding.contact = item
            binding.executePendingBindings()
        }
    }

    inner class EmptyViewHolder(val binding: LayoutEmptyContactsBinding) : ViewHolder(binding.root) {
        fun bind()  {
            binding.message1 = ResourceProvider.getString(R.string.empty_contacts_msg1)
            binding.message2 = ResourceProvider.getString(R.string.empty_contacts_msg2)
            binding.message3 = ResourceProvider.getString(R.string.empty_contacts_msg3)
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_ITEM -> {
                DataViewHolder(
                    LayoutContactCardBinding.inflate(layoutInflater,parent,false)
                )
            }

            VIEW_TYPE_EMPTY -> {
                EmptyViewHolder(
                    LayoutEmptyContactsBinding.inflate(layoutInflater,parent,false)
                )
            }

            else -> {
                throw IllegalArgumentException("Invalid view type")
            }
        }
    }

    override fun getItemCount(): Int {
        return if (items.isEmpty()) 1 else items.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (items.isEmpty()) VIEW_TYPE_EMPTY else VIEW_TYPE_ITEM
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder) {
            is DataViewHolder -> {
                holder.bind(items[position])
            }

            is EmptyViewHolder -> {
                holder.bind()
            }
        }
    }


    companion object {
        private const val VIEW_TYPE_EMPTY = 0
        private const val VIEW_TYPE_ITEM = 1
    }

}


