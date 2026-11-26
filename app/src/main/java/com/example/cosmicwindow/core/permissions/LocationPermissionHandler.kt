package com.example.cosmicwindow.core.permissions

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import com.example.cosmicwindow.R

class LocationPermissionController(
    val state: LocationPermissionState,
    val requestPermission: () -> Unit,
    val openSettings: () -> Unit,
    val showSettingsDialog: Boolean,
    val dismissSettingsDialog: () -> Unit,
)

private val locationKey = "location_requested"
private val permission = Manifest.permission.ACCESS_COARSE_LOCATION

@Composable
fun rememberLocationPermissionState(): LocationPermissionController {
    val context = LocalContext.current

    val preferences =
        remember {
            context.getSharedPreferences(
                "permission_preferences",
                Context.MODE_PRIVATE,
            )
        }

    var hasRequestedPermission by remember {
        mutableStateOf(
            preferences.getBoolean(locationKey, false),
        )
    }

    var permissionState by remember {
        mutableStateOf(
            checkLocationPermission(context, hasRequestedPermission),
        )
    }

    var showSettingsDialog by remember {
        mutableStateOf(false)
    }

    val permissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
        ) { granted ->

            hasRequestedPermission = true

            preferences.edit {
                putBoolean(locationKey, true)
            }

            permissionState =
                checkLocationPermission(
                    context,
                    hasRequestedPermission,
                )

            if (!granted) {
                permissionState =
                    checkLocationPermission(
                        context,
                        true,
                    )
            }
        }

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        permissionState =
            checkLocationPermission(
                context,
                hasRequestedPermission,
            )

        if (permissionState == LocationPermissionState.GRANTED) {
            showSettingsDialog = false
            Log.w("permissionHandler", "Location permission granted")
        }
    }

    val requestPermission: () -> Unit = {
        permissionState =
            checkLocationPermission(
                context,
                hasRequestedPermission,
            )

        when (permissionState) {
            LocationPermissionState.GRANTED -> Unit

            LocationPermissionState.DENIED_PERMANENTLY -> {
                showSettingsDialog = true
            }

            else -> {
                permissionLauncher.launch(
                    permission,
                )
            }
        }
    }

    val openSettings: () -> Unit = {
        showSettingsDialog = false

        val intent =
            Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", context.packageName, null),
            )

        context.startActivity(intent)
    }

    return LocationPermissionController(
        state = permissionState,
        requestPermission = requestPermission,
        openSettings = openSettings,
        showSettingsDialog = showSettingsDialog,
        dismissSettingsDialog = {
            showSettingsDialog = false
        },
    )
}

private fun checkLocationPermission(context: Context, previouslyRequested: Boolean): LocationPermissionState {
    if (
        ContextCompat.checkSelfPermission(
            context,
            permission,
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        return LocationPermissionState.GRANTED
    }

    val activity = context as Activity

    return when {
        !previouslyRequested ->
            LocationPermissionState.NOT_REQUESTED

        ActivityCompat.shouldShowRequestPermissionRationale(
            activity,
            permission,
        ) ->
            LocationPermissionState.DENIED

        else ->
            LocationPermissionState.DENIED_PERMANENTLY
    }
}

@Composable
fun LocationPermissionSettingsDialog(onOpenSettings: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(stringResource(R.string.location_permission_window_title))
        },
        text = {
            Text(stringResource(R.string.location_permission_window_description))
        },
        confirmButton = {
            TextButton(
                onClick = onOpenSettings,
            ) {
                Text(stringResource(R.string.open_settings_button_label))
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
            ) {
                Text(stringResource(R.string.cancel_button_label))
            }
        },
    )
}
