package com.shivamgupta.callkeeper.feature_contacts.presentation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.shivamgupta.callkeeper.feature_contacts.presentation.settings.SettingsFragment
import com.shivamgupta.callkeeper.feature_contacts.presentation.add_edit_contact.HomeFragment
import com.shivamgupta.callkeeper.feature_contacts.presentation.history.HistoryFragment

class MainViewPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager,lifecycle) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
       return when(position){
            0 -> HomeFragment()
            1 -> HistoryFragment()
            2 -> SettingsFragment()
            else -> {
                throw Exception("Invalid position")
            }
        }
    }
}