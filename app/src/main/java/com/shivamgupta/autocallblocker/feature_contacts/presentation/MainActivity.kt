package com.shivamgupta.autocallblocker.feature_contacts.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import com.google.android.material.tabs.TabLayoutMediator
import com.shivamgupta.autocallblocker.databinding.ActivityMainBinding
import com.shivamgupta.autocallblocker.feature_contacts.util.Constants

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupMainTabs()
    }

    private fun setupMainTabs() = with(binding) {
        viewPagerLayout.adapter = MainViewPagerAdapter(supportFragmentManager, lifecycle)

        TabLayoutMediator(mainTabLayout, viewPagerLayout) { tab, position ->
            tab.text = Constants.tabsName[position]
            tab.icon = AppCompatResources.getDrawable(this@MainActivity, Constants.tabsIconsRes[position])
        }.attach()
    }
}