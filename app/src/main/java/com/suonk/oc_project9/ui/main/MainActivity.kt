package com.suonk.oc_project9.ui.main

import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.suonk.oc_project9.R
import com.suonk.oc_project9.databinding.ActivityMainBinding
import com.suonk.oc_project9.ui.real_estates.details.RealEstateDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private val viewModel by viewModels<RealEstateDetailsViewModel>()

    var uri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        setupActionBar(binding)
    }

    //region =========================================== TOOLBAR ============================================

    private fun setupActionBar(binding: ActivityMainBinding) {
        binding.toolbar.apply {
            setSupportActionBar(this)
            setBackgroundColor(ContextCompat.getColor(this@MainActivity, R.color.light_grey))
        }

        supportActionBar?.title = ""
    }

    //endregion
}