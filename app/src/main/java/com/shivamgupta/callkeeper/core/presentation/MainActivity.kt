package com.shivamgupta.callkeeper.core.presentation

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.lifecycleScope
import com.google.android.material.tabs.TabLayoutMediator
import com.shivamgupta.callkeeper.databinding.ActivityMainBinding
import com.shivamgupta.callkeeper.core.PhoneCallService
import com.shivamgupta.callkeeper.util.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val settingsViewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupMainTabs()
        setupPhoneCallService()
    }

    private fun setupPhoneCallService() {
        val serviceIntent = Intent(this@MainActivity,PhoneCallService::class.java)
        lifecycleScope.launch(Dispatchers.Main) {
            settingsViewModel.isCallRejectionEnabled.collectLatest { isEnabled ->
                if(isEnabled){
                    startService(serviceIntent)
                } else {
                    try {
                        stopService(serviceIntent)
                    } catch (e: IllegalArgumentException) {
                        //TODO("Log exception here using timber")
                    }
                }
            }
        }
    }

    private fun setupMainTabs() = with(binding) {
        viewPagerLayout.adapter = MainViewPagerAdapter(supportFragmentManager, lifecycle)

        TabLayoutMediator(mainTabLayout, viewPagerLayout) { tab, position ->
            tab.text = Constants.tabsName[position]
            tab.icon = AppCompatResources.getDrawable(this@MainActivity, Constants.tabsIconsRes[position])
        }.attach()
    }
}