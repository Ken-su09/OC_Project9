package com.suonk.oc_project9.ui.real_estates.details

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import com.suonk.oc_project9.R
import com.suonk.oc_project9.databinding.ActivityDetailsBinding
import com.suonk.oc_project9.ui.main.MainActivity
import com.suonk.oc_project9.ui.real_estates.list.RealEstatesListFragment
import com.suonk.oc_project9.utils.showToast
import com.suonk.oc_project9.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {

    private val binding by viewBinding { ActivityDetailsBinding.inflate(it) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupToolbar()

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(R.id.details_fragment_container, RealEstateDetailsFragment())
                .addToBackStack(null).commit()
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                startActivity(Intent(this@DetailsActivity, MainActivity::class.java))
                finish()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
    }
}