package com.akshit.weatherchallenge.utils

import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.akshit.weatherchallenge.R
import com.google.android.material.snackbar.Snackbar

const val LOCATION_PERMISSION_REQUEST_CODE = 129

fun requestPermission(
    activity: AppCompatActivity,
    requestId: Int,
    permission: String,
    error: String
) {
    if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
        // Display a dialog with rationale.

        AlertDialog.Builder(activity).apply {
            setTitle(context.getString(R.string.permission_rationale_title))
            setMessage(context.getString(R.string.permission_rationale_message))
            setPositiveButton(context.getString(R.string.ok)) { _: DialogInterface, _: Int ->
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(permission),
                    requestId
                )
            }
            setNegativeButton(context.getString(R.string.no_thanks)) { _: DialogInterface, _: Int ->
                val view: ViewGroup = activity.window.decorView.findViewById(android.R.id.content)
                Snackbar.make(view, error, Snackbar.LENGTH_INDEFINITE).show()
            }
            show()
        }
    } else {
        // Location permission has not been granted yet, request it.
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(permission),
            requestId
        )
    }
}

fun hasPermission(context: Context, permission: String): Boolean {
    return ContextCompat.checkSelfPermission(
        context,
        permission
    ) == PackageManager.PERMISSION_GRANTED
}

fun isPermissionGranted(
    grantPermissions: Array<String>,
    grantResults: IntArray,
    permission: String
): Boolean {
    for (i in grantPermissions.indices) {
        if (permission == grantPermissions[i]) {
            return grantResults[i] == PackageManager.PERMISSION_GRANTED
        }
    }
    return false
}
