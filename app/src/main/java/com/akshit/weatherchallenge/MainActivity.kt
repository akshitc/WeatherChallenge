package com.akshit.weatherchallenge

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.akshit.weatherchallenge.databinding.ActivityMainBinding
import com.akshit.weatherchallenge.di.Injection
import com.akshit.weatherchallenge.utils.LOCATION_PERMISSION_REQUEST_CODE
import com.akshit.weatherchallenge.utils.hasPermission
import com.akshit.weatherchallenge.utils.isPermissionGranted
import com.akshit.weatherchallenge.utils.requestPermission
import com.akshit.weatherchallenge.viewModels.WeatherViewModel
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private val viewModel: WeatherViewModel by viewModels { Injection.provideViewModelFactory() }
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        requestLocationPermission()
        setObservers()
    }

    private fun requestLocationPermission() {
        if (hasPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            getCurrentLocation()
        } else {
            requestPermission(
                this,
                LOCATION_PERMISSION_REQUEST_CODE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                getString(R.string.error_text)
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return
        }
        if (isPermissionGranted(
                permissions,
                grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            getCurrentLocation()
        } else {
            showError()
        }
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {
        LocationServices.getFusedLocationProviderClient(this).lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                viewModel.getWeather(location.latitude, location.longitude)
            } else {
                showError()
            }
        }.addOnFailureListener {
            showError()
        }
    }

    private fun setObservers() {
        viewModel.weatherUpdate.observe(this) {
            binding.temperature.text = it.temperature
            binding.description.text = it.description
        }

        viewModel.loading.observe(this) { isLoading ->
            binding.progressBar.isVisible = isLoading
        }

        viewModel.error.observe(this) {
            showError()
        }
    }

    private fun showError() {
        val view: ViewGroup = this.window.decorView.findViewById(android.R.id.content)
        Snackbar.make(view, R.string.error_text, Snackbar.LENGTH_INDEFINITE).show()
    }
}
