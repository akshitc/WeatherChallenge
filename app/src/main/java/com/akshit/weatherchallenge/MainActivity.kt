package com.akshit.weatherchallenge

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.akshit.weatherchallenge.utils.LOCATION_PERMISSION_REQUEST_CODE
import com.akshit.weatherchallenge.utils.hasPermission
import com.akshit.weatherchallenge.utils.isPermissionGranted
import com.akshit.weatherchallenge.utils.requestPermission
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestLocationPermission()
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
                Toast.makeText(
                    this,
                    location.latitude.toString() + ", " + location.longitude.toString(),
                    Toast.LENGTH_LONG
                ).show()
            } else {
                showError()
            }
        }.addOnFailureListener {
            showError()
        }
    }

    private fun showError() {
        val view: ViewGroup = this.window.decorView.findViewById(android.R.id.content)
        Snackbar.make(view, R.string.error_text, Snackbar.LENGTH_INDEFINITE).show()
    }
}
