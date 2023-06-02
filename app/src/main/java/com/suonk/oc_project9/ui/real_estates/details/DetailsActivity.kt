package com.suonk.oc_project9.ui.real_estates.details

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.suonk.oc_project9.R
import com.suonk.oc_project9.databinding.ActivityDetailsBinding
import com.suonk.oc_project9.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {

    private val binding by viewBinding { ActivityDetailsBinding.inflate(it) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction().replace(R.id.details_fragment_container, RealEstateDetailsFragment()).addToBackStack(null)
            .commit()
    }
}