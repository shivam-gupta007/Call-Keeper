package com.shivamgupta.callkeeper.core.presentation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.shivamgupta.callkeeper.contacts.presentation.home.HomeFragment
import com.shivamgupta.callkeeper.history.presentation.HistoryFragment

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