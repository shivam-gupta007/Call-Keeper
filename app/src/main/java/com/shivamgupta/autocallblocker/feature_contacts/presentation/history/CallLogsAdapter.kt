package com.shivamgupta.autocallblocker.feature_contacts.presentation.history


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.shivamgupta.autocallblocker.R
import com.shivamgupta.autocallblocker.databinding.LayoutCallLogCardBinding
import com.shivamgupta.autocallblocker.databinding.LayoutEmptyDataBinding
import com.shivamgupta.autocallblocker.feature_contacts.domain.model.CallLog

class CallLogsAdapter(private val items: List<CallLog>) :
    RecyclerView.Adapter<ViewHolder>() {

    inner class DataViewHolder(val binding: LayoutCallLogCardBinding) :
        ViewHolder(binding.root) {
        fun bind(item: CallLog) = with(binding) {
            callLog = item
            executePendingBindings()
        }
    }

    inner class EmptyViewHolder(val binding: LayoutEmptyDataBinding) : ViewHolder(binding.root) {
        fun bind() = with(binding) {
            message = root.resources.getString(R.string.empty_call_log_message)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            VIEW_TYPE_ITEM -> {
                DataViewHolder(
                    DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context),
                        R.layout.layout_call_log_card,
                        parent,
                        false
                    )
                )
            }

            VIEW_TYPE_EMPTY -> {
                EmptyViewHolder(
                    DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context),
                        R.layout.layout_contact_card,
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


