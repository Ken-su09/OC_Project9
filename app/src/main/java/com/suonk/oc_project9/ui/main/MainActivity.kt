package com.suonk.oc_project9.ui.main

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.suonk.oc_project9.R
import com.suonk.oc_project9.databinding.ActivityMainBinding
import com.suonk.oc_project9.ui.real_estates.details.DetailsActivity
import com.suonk.oc_project9.ui.real_estates.details.RealEstateDetailsFragment
import com.suonk.oc_project9.ui.real_estates.list.RealEstatesListFragment
import com.suonk.oc_project9.utils.Constants
import com.suonk.oc_project9.utils.Event.Companion.observeEvent
import com.suonk.oc_project9.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding by viewBinding { ActivityMainBinding.inflate(it) }
    private val viewModel by viewModels<MainViewModel>()

    private companion object {
        private const val STORAGE_PERMISSION_CODE = 100
        private const val TAG = "PERMISSION_TAG"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

//        if (savedInstanceState == null) {
//            // Activity is being recreated
//        }

        requestPermission()
        isMapsEnabled()
        getLocationPermission()
    }

    private fun displayUi() {
        isMapsEnabled()
        getLocationPermission()

        replaceFragment(R.id.fragment_container, RealEstatesListFragment(), "RealEstatesListFragment")

        viewModel.mainViewAction.observeEvent(this) { action ->
            Log.i("TryToCollect", "MainActivity action : $action")

            when (action) {
                is MainViewAction.Navigate.Detail -> {
                    Log.i("TryToCollect", "MainViewAction.Navigate.Detail : $action")

                    startActivity(Intent(this@MainActivity, DetailsActivity::class.java))
                    finish()
                }
            }
        }

        if (resources.getBoolean(R.bool.isTablet)) {
            replaceFragment(R.id.fragment_container_details, RealEstateDetailsFragment(), "RealEstateDetailsFragment")
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.onResume(resources.getBoolean(R.bool.isTablet))
    }

    override fun onStop() {
        super.onStop()
        viewModel.onStop()
    }

    //region ============================================================= GOOGLE MAPS ==============================================================

    private fun alertDialogGpsIsDisabled() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(getString(R.string.gps_disabled_msg)).setCancelable(false)
            .setPositiveButton(getString(R.string.positive_button)) { _: DialogInterface?, id: Int ->
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivityForResult(intent, Constants.PERMISSIONS_REQUEST_ENABLE_GPS)
            }.create().show()
    }

    private fun isMapsEnabled() {
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            alertDialogGpsIsDisabled()
        }
    }

    private fun getLocationPermission() {
        viewModel.getPermissionsLiveData().observe(this) { isPermissionsEnabled ->
            if (!isPermissionsEnabled) {
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), Constants.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
                )
            } else {
                replaceFragment(R.id.fragment_container, RealEstatesListFragment(), "RealEstatesListFragment")
            }
        }
    }

    //endregion

    //region ================================================================ REQUEST ===============================================================

    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                val intent = Intent()
                intent.action = Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
                val uri = Uri.fromParts("package", this.packageName, null)
                intent.data = uri
                storageActivityResultLauncher.launch(intent)
            } catch (e: Exception) {
                Log.e(TAG, "requestPermission: ", e)
                val intent = Intent()
                intent.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
                storageActivityResultLauncher.launch(intent)
            }
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE),
                STORAGE_PERMISSION_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                displayUi()
            } else {
                Toast.makeText(this, getString(R.string.storage_permission_toast), Toast.LENGTH_LONG).show()
                requestPermission()
            }
        }
    }

    private val storageActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
                displayUi()
            } else {
                Toast.makeText(this, getString(R.string.storage_permission_toast), Toast.LENGTH_LONG).show()
                requestPermission()
            }
        } else {
            Toast.makeText(this, getString(R.string.storage_permission_toast), Toast.LENGTH_LONG).show()
            requestPermission()
        }
    }

    //endregion

    private fun replaceFragment(fragmentContainerId: Int, fragment: Fragment, tag: String) {
        supportFragmentManager.beginTransaction().replace(fragmentContainerId, fragment, tag).addToBackStack(null).commit()
    }
}