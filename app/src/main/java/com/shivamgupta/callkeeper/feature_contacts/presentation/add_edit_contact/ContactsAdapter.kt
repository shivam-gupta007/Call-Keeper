package com.shivamgupta.callkeeper.feature_contacts.presentation.add_edit_contact


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.shivamgupta.callkeeper.R
import com.shivamgupta.callkeeper.databinding.LayoutContactCardBinding
import com.shivamgupta.callkeeper.databinding.LayoutEmptyDataBinding
import com.shivamgupta.callkeeper.feature_contacts.domain.model.Contact


class ContactsAdapter(
    private val items: List<Contact>,
    private val onItemClick: (contact: Contact) -> Unit,
    private val onContactSelect: (Int, Boolean) -> Unit
) : RecyclerView.Adapter<ViewHolder>() {

    inner class DataViewHolder(val binding: LayoutContactCardBinding) :
        ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition.takeIf { it != RecyclerView.NO_POSITION } ?: return@setOnClickListener
                onItemClick(items[position])
            }

            binding.contactSelectIv.apply {
                setOnClickListener {
                    val position = adapterPosition.takeIf { it != RecyclerView.NO_POSITION } ?: return@setOnClickListener
                    tag = if (tag == "unchecked") "checked" else "unchecked"
                    setImageResource(if (tag == "unchecked") R.drawable.ic_circle else R.drawable.ic_circle_check)

                    val isSelected = (tag == "checked")
                    onContactSelect(position, isSelected)
                }
            }

        }

        fun bind(item: Contact) = with(binding) {
            contact = item
            executePendingBindings()
        }
    }

    inner class EmptyViewHolder(val binding: LayoutEmptyDataBinding) : ViewHolder(binding.root) {
        fun bind() = with(binding) {
            message = root.resources.getString(R.string.empty_contacts_msg)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            VIEW_TYPE_ITEM -> {
                DataViewHolder(
                    DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context),
                        R.layout.layout_contact_card,
                        parent,
                        false
                    )
                )
            }

            VIEW_TYPE_EMPTY -> {
                EmptyViewHolder(
                    DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context),
                        R.layout.layout_empty_data,
                        parent,
                        false
                    )

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


