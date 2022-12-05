package com.suonk.oc_project9.ui.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.navigation.NavigationView
import com.suonk.oc_project9.R
import com.suonk.oc_project9.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var navController: NavController
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        setupActionBar(binding)
        setupDrawerLayout(binding)
        setupBottomNavigationMenu(binding)
    }

    //region =========================================== TOOLBAR ============================================

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            else -> {
                if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
                    true
                } else super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_toolbar_menu, menu)

        val searchItem = menu?.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as SearchView

        return super.onCreateOptionsMenu(menu)
    }

    private fun setupActionBar(binding: ActivityMainBinding) {
        binding.toolbar.apply {
            setSupportActionBar(this)
            setBackgroundColor(ContextCompat.getColor(this@MainActivity, R.color.light_grey))
        }

        supportActionBar?.title = ""
    }

    //endregion

    //region ================================== SETUP MENU/TOOLBAR/DRAWER ===================================
    //region ================================== SETUP MENU/TOOLBAR/DRAWER ===================================

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return true
    }

    private fun setupDrawerLayout(binding: ActivityMainBinding) {
        actionBarDrawerToggle = ActionBarDrawerToggle(
            this, binding.drawerLayout, binding.toolbar,
            R.string.drawer_opened, R.string.drawer_closed
        )
        binding.drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.isDrawerIndicatorEnabled = true
        actionBarDrawerToggle.syncState()
    }

    private fun setupBottomNavigationMenu(binding: ActivityMainBinding) {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_real_estate -> {
                    navController.navigate(R.id.realEstatesListFragment)
                }
                else -> {
                    navController.navigate(R.id.realEstatesListFragment)
                }
            }
            true
        }
    }

//    private fun setupVisibility(binding: ActivityMainBinding) {
//        viewModel.isVisibleLiveData.observe(this) { isVisible ->
//            Log.i("testId", "$isVisible")
//            binding.drawerLayout.isVisible = isVisible
//            binding.bottomNavigation.isVisible = isVisible
//            binding.toolbar.isVisible = isVisible
//        }
//    }

    //endregion

    private val REQUEST_CODE = 1
    private var imageUri: Uri? = null

    private fun openGallery() {
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(gallery, REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            imageUri = data?.data
        }
    }

//    override fun onItem
}